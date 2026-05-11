<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '../../api/admin'

const items = ref([])
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', slug: '', color: '#2563eb' })

function reset(item = null) {
  editingId.value = item?.id || null
  Object.assign(form, {
    name: item?.name || '',
    slug: item?.slug || '',
    color: item?.color || '#2563eb'
  })
  dialogVisible.value = true
}

async function load() {
  items.value = await adminApi.tags()
}

async function save() {
  if (editingId.value) {
    await adminApi.updateTag(editingId.value, form)
  } else {
    await adminApi.createTag(form)
  }
  ElMessage.success('Tag saved')
  dialogVisible.value = false
  load()
}

async function remove(row) {
  await ElMessageBox.confirm(`Delete "${row.name}"?`, 'Delete Tag', { type: 'warning' })
  await adminApi.deleteTag(row.id)
  ElMessage.success('Tag deleted')
  load()
}

onMounted(load)
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Tag Management</h1>
        <p>Reusable marks for article details.</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="reset()">New Tag</el-button>
    </div>

    <div class="admin-panel">
      <el-table :data="items">
        <el-table-column prop="name" label="Name" min-width="160" />
        <el-table-column prop="slug" label="Slug" min-width="160" />
        <el-table-column label="Color" width="140">
          <template #default="{ row }">
            <el-tag :color="row.color" effect="plain">{{ row.color }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <el-button :icon="Edit" circle @click="reset(row)" />
            <el-button :icon="Delete" circle type="danger" @click="remove(row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="Tag" width="420px">
      <el-form label-position="top">
        <el-form-item label="Name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="Slug"><el-input v-model="form.slug" placeholder="Auto-generated if empty" /></el-form-item>
        <el-form-item label="Color"><el-color-picker v-model="form.color" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </template>
    </el-dialog>
  </section>
</template>
