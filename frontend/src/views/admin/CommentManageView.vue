<script setup>
import { onMounted, ref } from 'vue'
import { Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '../../api/admin'
import { formatDateTime } from '../../utils/format'

const loading = ref(false)
const comments = ref([])

async function load() {
  loading.value = true
  try {
    comments.value = await adminApi.comments()
  } finally {
    loading.value = false
  }
}

async function toggle(row) {
  await adminApi.updateComment(row.id, { approved: row.approved })
  ElMessage.success('Comment updated')
}

async function remove(row) {
  await ElMessageBox.confirm('Delete this comment?', 'Delete Comment', { type: 'warning' })
  await adminApi.deleteComment(row.id)
  ElMessage.success('Comment deleted')
  load()
}

onMounted(load)
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Comment Management</h1>
        <p>Visitor messages and moderation state.</p>
      </div>
    </div>

    <div class="admin-panel">
      <el-table v-loading="loading" :data="comments">
        <el-table-column prop="authorName" label="Author" width="150" />
        <el-table-column prop="content" label="Comment" min-width="260" />
        <el-table-column label="Approved" width="130">
          <template #default="{ row }">
            <el-switch v-model="row.approved" @change="toggle(row)" />
          </template>
        </el-table-column>
        <el-table-column label="Created" width="170">
          <template #default="{ row }">{{ formatDateTime(row.createdAt) }}</template>
        </el-table-column>
        <el-table-column label="Actions" width="100" fixed="right">
          <template #default="{ row }">
            <el-button :icon="Delete" circle type="danger" @click="remove(row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>
  </section>
</template>
