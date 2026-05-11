<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import SiteLayout from './components/SiteLayout.vue'
import AdminLayout from './components/AdminLayout.vue'

const route = useRoute()
const isAdminPage = computed(() => route.meta.admin === true)
const isLoginPage = computed(() => route.name === 'admin-login')
</script>

<template>
  <router-view v-if="isLoginPage" />
  <AdminLayout v-else-if="isAdminPage">
    <router-view />
  </AdminLayout>
  <SiteLayout v-else>
    <router-view />
  </SiteLayout>
</template>
