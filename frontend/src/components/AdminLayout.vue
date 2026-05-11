<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  ChatDotRound,
  Collection,
  Files,
  House,
  Picture,
  PriceTag,
  SwitchButton
} from '@element-plus/icons-vue'
import { clearSession, getUser } from '../utils/auth'

const router = useRouter()
const route = useRoute()
const user = computed(() => getUser())

function logout() {
  clearSession()
  router.push({ name: 'admin-login' })
}
</script>

<template>
  <el-container class="admin-shell">
    <el-aside width="236px" class="admin-aside">
      <div class="admin-brand">
        <span class="brand__mark">
          <el-icon><House /></el-icon>
        </span>
        <span>Admin Console</span>
      </div>
      <el-menu :default-active="route.path" router>
        <el-menu-item index="/admin">
          <el-icon><House /></el-icon>
          <span>Dashboard</span>
        </el-menu-item>
        <el-menu-item index="/admin/articles">
          <el-icon><Files /></el-icon>
          <span>Articles</span>
        </el-menu-item>
        <el-menu-item index="/admin/categories">
          <el-icon><Collection /></el-icon>
          <span>Categories</span>
        </el-menu-item>
        <el-menu-item index="/admin/tags">
          <el-icon><PriceTag /></el-icon>
          <span>Tags</span>
        </el-menu-item>
        <el-menu-item index="/admin/comments">
          <el-icon><ChatDotRound /></el-icon>
          <span>Comments</span>
        </el-menu-item>
        <el-menu-item index="/admin/images">
          <el-icon><Picture /></el-icon>
          <span>Images</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="admin-header">
        <strong>{{ user?.displayName || user?.username || 'Admin' }}</strong>
        <el-button :icon="SwitchButton" @click="logout">Logout</el-button>
      </el-header>
      <el-main class="admin-main">
        <slot />
      </el-main>
    </el-container>
  </el-container>
</template>
