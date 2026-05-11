<script setup>
import { onMounted, ref } from 'vue'
import CommentPanel from '../components/CommentPanel.vue'
import { publicApi } from '../api/public'

const comments = ref([])

async function loadComments() {
  try {
    comments.value = await publicApi.comments({ size: 30 })
  } catch {
    comments.value = []
  }
}

onMounted(loadComments)
</script>

<template>
  <section class="page-section">
    <div class="page-heading">
      <p class="kicker">Comment Board</p>
      <h1>Leave a note for the blog.</h1>
      <p>Messages from visitors and readers.</p>
    </div>
    <CommentPanel :comments="comments" @created="loadComments" />
  </section>
</template>
