<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { publicApi } from '../api/public'
import { formatDateTime } from '../utils/format'

const props = defineProps({
  articleId: {
    type: Number,
    default: null
  },
  comments: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['created'])
const submitting = ref(false)
const form = reactive({
  authorName: '',
  authorEmail: '',
  content: ''
})

async function submit() {
  if (!form.authorName || !form.content) {
    ElMessage.warning('Name and comment are required')
    return
  }
  submitting.value = true
  try {
    await publicApi.createComment({
      articleId: props.articleId,
      authorName: form.authorName,
      authorEmail: form.authorEmail,
      content: form.content
    })
    form.authorName = ''
    form.authorEmail = ''
    form.content = ''
    ElMessage.success('Comment submitted for review')
    emit('created')
  } finally {
    submitting.value = false
  }
}
</script>

<template>
  <section class="panel">
    <h3>Comments</h3>
    <div class="comment-list">
      <article v-for="comment in comments" :key="comment.id" class="comment-card">
        <div class="article-card__meta">
          <strong>{{ comment.authorName }}</strong>
          <time>{{ formatDateTime(comment.createdAt) }}</time>
        </div>
        <p>{{ comment.content }}</p>
      </article>
      <el-empty v-if="!comments.length" description="No comments yet" />
    </div>
    <el-form class="comment-form" label-position="top" @submit.prevent>
      <el-form-item label="Name">
        <el-input v-model="form.authorName" maxlength="40" />
      </el-form-item>
      <el-form-item label="Email">
        <el-input v-model="form.authorEmail" maxlength="120" />
      </el-form-item>
      <el-form-item label="Comment">
        <el-input v-model="form.content" type="textarea" :rows="4" maxlength="1000" show-word-limit />
      </el-form-item>
      <el-button type="primary" :loading="submitting" @click="submit">Submit</el-button>
    </el-form>
  </section>
</template>
