<script setup>
import { onMounted, ref } from 'vue'
import { Delete, UploadFilled } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { adminApi } from '../../api/admin'
import { absoluteAssetUrl } from '../../utils/format'

const images = ref([])
const uploading = ref(false)

async function load() {
  images.value = await adminApi.images()
}

async function customUpload(options) {
  uploading.value = true
  try {
    const image = await adminApi.uploadImage(options.file)
    options.onSuccess(image)
    ElMessage.success('Image uploaded')
    load()
  } finally {
    uploading.value = false
  }
}

async function remove(image) {
  await ElMessageBox.confirm(`Delete "${image.originalName}"?`, 'Delete Image', { type: 'warning' })
  await adminApi.deleteImage(image.id)
  ElMessage.success('Image deleted')
  load()
}

onMounted(load)
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>Image Upload</h1>
        <p>Cover images and article media.</p>
      </div>
    </div>

    <div class="admin-panel">
      <div class="admin-panel__body">
        <el-upload
          drag
          multiple
          accept="image/*"
          :show-file-list="false"
          :http-request="customUpload"
          :disabled="uploading"
        >
          <el-icon class="el-icon--upload"><UploadFilled /></el-icon>
          <div class="el-upload__text">Drop images here or click to upload</div>
        </el-upload>
      </div>
    </div>

    <div class="image-grid">
      <div v-for="image in images" :key="image.id" class="image-tile">
        <img :src="absoluteAssetUrl(image.url)" :alt="image.originalName" />
        <div class="image-tile__footer">
          <span class="image-tile__name">{{ image.originalName }}</span>
          <el-button :icon="Delete" circle type="danger" @click="remove(image)" />
        </div>
      </div>
    </div>
  </section>
</template>
