<script setup>
import { Calendar, View } from '@element-plus/icons-vue'
import { absoluteAssetUrl, formatDate } from '../utils/format'

defineProps({
  article: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <RouterLink class="article-card" :to="`/articles/${article.slug}`">
    <div class="article-card__image">
      <img :src="absoluteAssetUrl(article.coverUrl) || '/images/cover.jpg'" :alt="article.title" />
    </div>
    <div class="article-card__body">
      <div class="article-card__meta">
        <el-tag v-for="category in article.categories || []" :key="category.id" effect="plain">
          {{ category.name }}
        </el-tag>
      </div>
      <h3>{{ article.title }}</h3>
      <p>{{ article.summary }}</p>
      <div class="article-card__meta">
        <el-tag type="info" effect="plain">
          <el-icon><Calendar /></el-icon>
          {{ formatDate(article.publishedAt || article.createdAt) }}
        </el-tag>
        <el-tag type="info" effect="plain">
          <el-icon><View /></el-icon>
          {{ article.viewCount || 0 }}
        </el-tag>
      </div>
    </div>
  </RouterLink>
</template>
