<script setup>
import { onMounted, ref } from 'vue'
import { ChatDotRound, Files, Picture, PriceTag } from '@element-plus/icons-vue'
import { adminApi } from '../../api/admin'

const stats = ref({
  articleCount: 0,
  draftCount: 0,
  pendingCommentCount: 0,
  imageCount: 0
})

onMounted(async () => {
  stats.value = await adminApi.dashboard()
})
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Dashboard</h1>
        <p>Content status at a glance.</p>
      </div>
    </div>
    <div class="taxonomy-grid">
      <div class="taxonomy-card">
        <el-icon size="26" color="#2563eb"><Files /></el-icon>
        <h3>{{ stats.articleCount }}</h3>
        <p>Articles</p>
      </div>
      <div class="taxonomy-card">
        <el-icon size="26" color="#0ea5e9"><PriceTag /></el-icon>
        <h3>{{ stats.draftCount }}</h3>
        <p>Drafts</p>
      </div>
      <div class="taxonomy-card">
        <el-icon size="26" color="#b7791f"><ChatDotRound /></el-icon>
        <h3>{{ stats.pendingCommentCount }}</h3>
        <p>Pending comments</p>
      </div>
      <div class="taxonomy-card">
        <el-icon size="26" color="#0f9f6e"><Picture /></el-icon>
        <h3>{{ stats.imageCount }}</h3>
        <p>Images</p>
      </div>
    </div>
  </section>
</template>
