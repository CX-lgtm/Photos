<script setup>
import { onMounted, ref } from 'vue'
import { Message, User } from '@element-plus/icons-vue'
import { publicApi } from '../api/public'
import { absoluteAssetUrl } from '../utils/format'

const profile = ref({
  name: 'Your Name',
  title: 'Photographer',
  bio: 'I photograph quiet light, passing weather, and ordinary places that become memorable through attention.',
  avatarUrl: '/images/cover.jpg',
  email: 'you@example.com'
})

onMounted(async () => {
  try {
    profile.value = await publicApi.profile()
  } catch {
    // Keep local fallback content when the API is offline.
  }
})
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <p class="kicker">About Me</p>
      <h1>{{ profile.name }}</h1>
      <p>{{ profile.title }}</p>
    </div>

    <div class="about-layout">
      <div class="about-photo">
        <img :src="absoluteAssetUrl(profile.avatarUrl) || '/images/cover.jpg'" :alt="profile.name" />
      </div>
      <div class="panel">
        <h3>
          <el-icon><User /></el-icon>
          Profile
        </h3>
        <p style="color: var(--muted); line-height: 1.8;">{{ profile.bio }}</p>
        <el-divider />
        <el-button type="primary" :icon="Message" tag="a" :href="`mailto:${profile.email}`">
          Contact
        </el-button>
      </div>
    </div>
  </section>
</template>
