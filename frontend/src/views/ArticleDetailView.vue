<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import CommentPanel from '../components/CommentPanel.vue'
import { publicApi } from '../api/public'
import { fallbackArticle } from '../data/fallback'
import { absoluteAssetUrl, formatDate } from '../utils/format'

const route = useRoute()
const article = ref(null)
const comments = ref([])
const loading = ref(false)

async function loadArticle() {
  loading.value = true
  try {
    article.value = await publicApi.article(route.params.slug)
    await loadComments()
  } catch {
    article.value = fallbackArticle
    comments.value = []
  } finally {
    loading.value = false
  }
}

async function loadComments() {
  if (!article.value?.id) return
  comments.value = await publicApi.comments({ articleId: article.value.id })
}

watch(() => route.params.slug, loadArticle)
onMounted(loadArticle)
</script>

<template>
  <section class="page-section">
    <el-skeleton v-if="loading" :rows="10" animated />
    <template v-else-if="article">
      <div class="page-heading">
        <p class="kicker">Article Details</p>
        <h1>{{ article.title }}</h1>
        <p>{{ article.summary }}</p>
        <div class="article-meta">
          <el-tag effect="plain">{{ formatDate(article.publishedAt || article.createdAt) }}</el-tag>
          <el-tag v-for="category in article.categories || []" :key="category.id" effect="plain">
            {{ category.name }}
          </el-tag>
          <el-tag v-for="tag in article.tags || []" :key="tag.id" type="info" effect="plain">
            {{ tag.name }}
          </el-tag>
        </div>
      </div>

      <div class="article-shell">
        <article>
          <div class="article-hero">
            <img :src="absoluteAssetUrl(article.coverUrl) || '/images/cover.jpg'" :alt="article.title" />
          </div>
          <div class="article-body" v-html="article.content"></div>
          <CommentPanel :article-id="article.id" :comments="comments" @created="loadComments" />
        </article>

        <aside class="sidebar">
          <div class="panel">
            <h3>Categories</h3>
            <div class="tag-row">
              <RouterLink
                v-for="category in article.categories || []"
                :key="category.id"
                :to="{ path: '/articles', query: { category: category.slug } }"
              >
                <el-tag effect="plain">{{ category.name }}</el-tag>
              </RouterLink>
            </div>
          </div>
          <div class="panel">
            <h3>Tags</h3>
            <div class="tag-row">
              <RouterLink
                v-for="tag in article.tags || []"
                :key="tag.id"
                :to="{ path: '/articles', query: { tag: tag.slug } }"
              >
                <el-tag type="info" effect="plain">{{ tag.name }}</el-tag>
              </RouterLink>
            </div>
          </div>
        </aside>
      </div>
    </template>
  </section>
</template>
