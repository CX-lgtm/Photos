<script setup>
import { computed, onMounted, ref } from 'vue'
import { ArrowRight, Picture } from '@element-plus/icons-vue'
import ArticleCard from '../components/ArticleCard.vue'
import { publicApi } from '../api/public'
import { fallbackArticles, fallbackCategories } from '../data/fallback'
import { absoluteAssetUrl } from '../utils/format'

const articles = ref([])
const categories = ref([])
const loading = ref(true)
const hero = computed(() => articles.value[0] || fallbackArticles[0])

async function load() {
  loading.value = true
  try {
    const [articlePage, categoryList] = await Promise.all([
      publicApi.articles({ page: 1, size: 6 }),
      publicApi.categories()
    ])
    articles.value = articlePage.records || []
    categories.value = categoryList || []
  } catch {
    articles.value = fallbackArticles
    categories.value = fallbackCategories
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>

<template>
  <section class="hero-section">
    <div class="hero-copy">
      <p class="kicker">Photography Journal</p>
      <h1>Keep light, color, and quiet stories in one place.</h1>
      <p>
        Recent frames, essays, and notes from ordinary places that stayed with the lens.
      </p>
      <div class="hero-actions">
        <el-button type="primary" size="large" :icon="Picture" @click="$router.push('/articles')">
          Browse Articles
        </el-button>
        <el-button size="large" :icon="ArrowRight" @click="$router.push('/about')">
          About Me
        </el-button>
      </div>
    </div>
    <div class="hero-media">
      <img :src="absoluteAssetUrl(hero.coverUrl) || '/images/cover.jpg'" :alt="hero.title" />
      <div class="hero-stat">
        <strong>{{ articles.length }}</strong>
        <span>published records</span>
      </div>
    </div>
  </section>

  <section class="page-section">
    <div class="section-header">
      <div>
        <h2>Latest Articles</h2>
        <p>Photo notes, gallery stories, and editing thoughts.</p>
      </div>
      <el-button :icon="ArrowRight" @click="$router.push('/articles')">View All</el-button>
    </div>
    <el-skeleton v-if="loading" :rows="6" animated />
    <div v-else class="article-grid">
      <ArticleCard v-for="article in articles.slice(0, 3)" :key="article.id" :article="article" />
    </div>
  </section>

  <section class="page-section">
    <div class="section-header">
      <div>
        <h2>Categories</h2>
        <p>Subjects and shooting moods from the archive.</p>
      </div>
    </div>
    <div class="taxonomy-grid">
      <RouterLink
        v-for="category in categories.slice(0, 6)"
        :key="category.id"
        class="taxonomy-card"
        :to="{ path: '/articles', query: { category: category.slug } }"
      >
        <el-tag effect="plain">{{ category.slug }}</el-tag>
        <h3>{{ category.name }}</h3>
        <p>{{ category.description || 'Photography notes and selected images.' }}</p>
      </RouterLink>
    </div>
  </section>
</template>
