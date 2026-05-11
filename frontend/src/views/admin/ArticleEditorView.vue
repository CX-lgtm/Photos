<script setup>
import { computed, onBeforeUnmount, onMounted, reactive, shallowRef, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import { ElMessage } from 'element-plus'
import { Picture, Select } from '@element-plus/icons-vue'
import { adminApi } from '../../api/admin'
import { absoluteAssetUrl } from '../../utils/format'

const route = useRoute()
const router = useRouter()
const editorRef = shallowRef()
const loading = ref(false)
const saving = ref(false)
const categories = ref([])
const tags = ref([])
const isEdit = computed(() => Boolean(route.params.id))
const form = reactive({
  title: '',
  slug: '',
  summary: '',
  coverUrl: '',
  content: '',
  status: 'DRAFT',
  categoryIds: [],
  tagIds: []
})

const toolbarConfig = {}
const editorConfig = {
  placeholder: 'Write the article content...',
  MENU_CONF: {
    uploadImage: {
      async customUpload(file, insertFn) {
        const image = await adminApi.uploadImage(file)
        const url = absoluteAssetUrl(image.url)
        insertFn(url, image.originalName, url)
      }
    }
  }
}

function handleCreated(editor) {
  editorRef.value = editor
}

async function uploadCover(options) {
  const image = await adminApi.uploadImage(options.file)
  form.coverUrl = image.url
  options.onSuccess(image)
}

async function load() {
  loading.value = true
  try {
    const [categoryList, tagList] = await Promise.all([adminApi.categories(), adminApi.tags()])
    categories.value = categoryList || []
    tags.value = tagList || []
    if (isEdit.value) {
      const article = await adminApi.article(route.params.id)
      Object.assign(form, {
        title: article.title || '',
        slug: article.slug || '',
        summary: article.summary || '',
        coverUrl: article.coverUrl || '',
        content: article.content || '',
        status: article.status || 'DRAFT',
        categoryIds: (article.categories || []).map((item) => item.id),
        tagIds: (article.tags || []).map((item) => item.id)
      })
    }
  } finally {
    loading.value = false
  }
}

async function save() {
  if (!form.title || !form.content) {
    ElMessage.warning('Title and content are required')
    return
  }
  saving.value = true
  try {
    if (isEdit.value) {
      await adminApi.updateArticle(route.params.id, form)
    } else {
      await adminApi.createArticle(form)
    }
    ElMessage.success('Article saved')
    router.push('/admin/articles')
  } finally {
    saving.value = false
  }
}

onMounted(load)
onBeforeUnmount(() => editorRef.value?.destroy())
</script>

<template>
  <section class="admin-page">
    <div class="admin-toolbar">
      <div class="admin-title">
        <h1>{{ isEdit ? 'Edit Article' : 'Publish Article' }}</h1>
        <p>Article text, cover image, categories, and tags.</p>
      </div>
      <el-button type="primary" :icon="Select" :loading="saving" @click="save">Save</el-button>
    </div>

    <div v-loading="loading" class="admin-panel">
      <div class="admin-panel__body">
        <el-form label-position="top">
          <el-row :gutter="18">
            <el-col :xs="24" :md="14">
              <el-form-item label="Title">
                <el-input v-model="form.title" maxlength="160" show-word-limit />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :md="6">
              <el-form-item label="Slug">
                <el-input v-model="form.slug" placeholder="Auto-generated if empty" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :md="4">
              <el-form-item label="Status">
                <el-select v-model="form.status">
                  <el-option label="Draft" value="DRAFT" />
                  <el-option label="Published" value="PUBLISHED" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="Summary">
            <el-input v-model="form.summary" type="textarea" :rows="3" maxlength="300" show-word-limit />
          </el-form-item>

          <el-row :gutter="18">
            <el-col :xs="24" :md="8">
              <el-form-item label="Cover Image">
                <el-upload :show-file-list="false" :http-request="uploadCover" accept="image/*">
                  <el-button :icon="Picture">Upload Cover</el-button>
                </el-upload>
                <el-input v-model="form.coverUrl" placeholder="/uploads/example.jpg or https://..." style="margin-top: 10px;" />
              </el-form-item>
            </el-col>
            <el-col :xs="24" :md="8">
              <el-form-item label="Categories">
                <el-select v-model="form.categoryIds" multiple collapse-tags collapse-tags-tooltip>
                  <el-option v-for="category in categories" :key="category.id" :label="category.name" :value="category.id" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :xs="24" :md="8">
              <el-form-item label="Tags">
                <el-select v-model="form.tagIds" multiple collapse-tags collapse-tags-tooltip>
                  <el-option v-for="tag in tags" :key="tag.id" :label="tag.name" :value="tag.id" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="Content">
            <div class="editor-wrapper">
              <Toolbar class="editor-toolbar" :editor="editorRef" :default-config="toolbarConfig" mode="default" />
              <Editor
                v-model="form.content"
                class="editor-content"
                :default-config="editorConfig"
                mode="default"
                @onCreated="handleCreated"
              />
            </div>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </section>
</template>
