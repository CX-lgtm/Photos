<script setup>
import { onMounted, ref } from 'vue'
import { publicApi } from '../api/public'
import { fallbackCategories, fallbackTags } from '../data/fallback'

const categories = ref([])
const tags = ref([])

async function load() {
  try {
    const [categoryList, tagList] = await Promise.all([publicApi.categories(), publicApi.tags()])
    categories.value = categoryList || []
    tags.value = tagList || []
  } catch {
    categories.value = fallbackCategories
    tags.value = fallbackTags
  }
}

onMounted(load)
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <p class="kicker">Categories/Tags</p>
      <h1>Browse by topic and shooting detail.</h1>
      <p>A map of broad collections and finer notes.</p>
    </div>

    <el-tabs>
      <el-tab-pane label="Categories">
        <div class="taxonomy-grid">
          <RouterLink
            v-for="category in categories"
            :key="category.id"
            class="taxonomy-card"
            :to="{ path: '/articles', query: { category: category.slug } }"
          >
            <el-tag effect="plain">{{ category.slug }}</el-tag>
            <h3>{{ category.name }}</h3>
            <p>{{ category.description || 'A collection of related photography records.' }}</p>
          </RouterLink>
        </div>
      </el-tab-pane>
      <el-tab-pane label="Tags">
        <div class="taxonomy-grid">
          <RouterLink
            v-for="tag in tags"
            :key="tag.id"
            class="taxonomy-card"
            :to="{ path: '/articles', query: { tag: tag.slug } }"
          >
            <el-tag :color="tag.color" effect="plain">{{ tag.slug }}</el-tag>
            <h3>{{ tag.name }}</h3>
            <p>All records marked with {{ tag.name }}.</p>
          </RouterLink>
        </div>
      </el-tab-pane>
    </el-tabs>
  </section>
</template>
