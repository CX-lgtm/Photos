<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useRouter } from 'vue-router'
import { adminApi } from '../../api/admin'
import { formatDateTime } from '../../utils/format'

const router = useRouter()
const loading = ref(false)
const articles = ref([])
const total = ref(0)
const page = reactive({ page: 1, size: 10 })

async function load() {
  loading.value = true
  try {
    const result = await adminApi.articles(page)
    articles.value = result.records || []
    total.value = result.total || 0
  } finally {
    loading.value = false
  }
}

async function remove(row) {
  await ElMessageBox.confirm(`Delete "${row.title}"?`, 'Delete Article', { type: 'warning' })
  await adminApi.deleteArticle(row.id)
  ElMessage.success('Article deleted')
  load()
}

onMounted(load)
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Articles</h1>
        <p>Journal entries and drafts.</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="router.push('/admin/articles/new')">New Article</el-button>
    </div>

    <div class="admin-panel">
      <el-table v-loading="loading" :data="articles">
        <el-table-column prop="title" label="Title" min-width="220" />
        <el-table-column prop="slug" label="Slug" min-width="160" />
        <el-table-column label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'" effect="plain">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Updated" width="170">
          <template #default="{ row }">{{ formatDateTime(row.updatedAt) }}</template>
        </el-table-column>
        <el-table-column label="Actions" width="170" fixed="right">
          <template #default="{ row }">
            <el-button :icon="Edit" circle @click="router.push(`/admin/articles/${row.id}/edit`)" />
            <el-button :icon="Delete" circle type="danger" @click="remove(row)" />
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex; justify-content: center; padding: 16px;">
        <el-pagination
          v-model:current-page="page.page"
          :page-size="page.size"
          layout="prev, pager, next"
          :total="total"
          @current-change="load"
        />
      </div>
    </div>
  </section>
</template>
