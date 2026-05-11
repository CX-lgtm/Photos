<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import ArticleCard from '../components/ArticleCard.vue'
import { publicApi } from '../api/public'
import { fallbackArticles, fallbackCategories, fallbackTags } from '../data/fallback'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const articles = ref([])
const categories = ref([])
const tags = ref([])
const total = ref(0)
const filters = reactive({
  page: Number(route.query.page || 1),
  size: 9,
  keyword: route.query.keyword || '',
  category: route.query.category || '',
  tag: route.query.tag || ''
})

async function loadOptions() {
  try {
    const [categoryList, tagList] = await Promise.all([publicApi.categories(), publicApi.tags()])
    categories.value = categoryList || []
    tags.value = tagList || []
  } catch {
    categories.value = fallbackCategories
    tags.value = fallbackTags
  }
}

async function loadArticles() {
  loading.value = true
  try {
    const page = await publicApi.articles(filters)
    articles.value = page.records || []
    total.value = page.total || 0
  } catch {
    articles.value = fallbackArticles
    total.value = fallbackArticles.length
  } finally {
    loading.value = false
  }
}

function syncQuery() {
  router.replace({
    query: {
      ...(filters.page > 1 ? { page: filters.page } : {}),
      ...(filters.keyword ? { keyword: filters.keyword } : {}),
      ...(filters.category ? { category: filters.category } : {}),
      ...(filters.tag ? { tag: filters.tag } : {})
    }
  })
}

function search() {
  filters.page = 1
  syncQuery()
  loadArticles()
}

watch(() => filters.page, () => {
  syncQuery()
  loadArticles()
})

onMounted(() => {
  loadOptions()
  loadArticles()
})
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <p class="kicker">Article List</p>
      <h1>Photo records and field notes.</h1>
      <p>Frames grouped by story, subject, and small technical notes.</p>
    </div>

    <div class="filter-bar">
      <el-input v-model="filters.keyword" placeholder="Search title or summary" clearable @keyup.enter="search">
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      <el-select v-model="filters.category" placeholder="Category" clearable @change="search">
        <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.slug" />
      </el-select>
      <el-select v-model="filters.tag" placeholder="Tag" clearable @change="search">
        <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.slug" />
      </el-select>
    </div>

    <el-skeleton v-if="loading" :rows="8" animated />
    <template v-else>
      <div class="article-grid">
        <ArticleCard v-for="article in articles" :key="article.id" :article="article" />
      </div>
      <el-empty v-if="!articles.length" description="No articles found" />
      <div style="display: flex; justify-content: center; margin-top: 24px;">
        <el-pagination
          v-model:current-page="filters.page"
          :page-size="filters.size"
          layout="prev, pager, next"
          :total="total"
        />
      </div>
    </template>
  </section>
</template>
