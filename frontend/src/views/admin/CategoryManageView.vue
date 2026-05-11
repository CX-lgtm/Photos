<script setup>
import { onMounted, reactive, ref } from 'vue'
import { Delete, Edit, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '../../api/admin'

const items = ref([])
const dialogVisible = ref(false)
const editingId = ref(null)
const form = reactive({ name: '', slug: '', description: '', sortOrder: 0 })

function reset(item = null) {
  editingId.value = item?.id || null
  Object.assign(form, {
    name: item?.name || '',
    slug: item?.slug || '',
    description: item?.description || '',
    sortOrder: item?.sortOrder || 0
  })
  dialogVisible.value = true
}

async function load() {
  items.value = await adminApi.categories()
}

async function save() {
  if (editingId.value) {
    await adminApi.updateCategory(editingId.value, form)
  } else {
    await adminApi.createCategory(form)
  }
  ElMessage.success('Category saved')
  dialogVisible.value = false
  load()
}

async function remove(row) {
  await ElMessageBox.confirm(`Delete "${row.name}"?`, 'Delete Category', { type: 'warning' })
  await adminApi.deleteCategory(row.id)
  ElMessage.success('Category deleted')
  load()
}

onMounted(load)
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Category Management</h1>
        <p>Broad groupings for the archive.</p>
      </div>
      <el-button type="primary" :icon="Plus" @click="reset()">New Category</el-button>
    </div>

    <div class="admin-panel">
      <el-table :data="items">
        <el-table-column prop="name" label="Name" min-width="160" />
        <el-table-column prop="slug" label="Slug" min-width="160" />
        <el-table-column prop="description" label="Description" min-width="220" />
        <el-table-column prop="sortOrder" label="Sort" width="90" />
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <el-button :icon="Edit" circle @click="reset(row)" />
            <el-button :icon="Delete" circle type="danger" @click="remove(row)" />
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" title="Category" width="460px">
      <el-form label-position="top">
        <el-form-item label="Name"><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="Slug"><el-input v-model="form.slug" placeholder="Auto-generated if empty" /></el-form-item>
        <el-form-item label="Description"><el-input v-model="form.description" type="textarea" /></el-form-item>
        <el-form-item label="Sort Order"><el-input-number v-model="form.sortOrder" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Save</el-button>
      </template>
    </el-dialog>
  </section>
</template>
