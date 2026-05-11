<script setup>
import { Camera, LogIn } from '@element-plus/icons-vue'
</script>

<template>
  <div class="site-shell">
    <header class="site-header">
      <div class="site-header__inner">
        <RouterLink class="brand" to="/">
          <span class="brand__mark">
            <el-icon><Camera /></el-icon>
          </span>
          <span>Photography Journal</span>
        </RouterLink>
        <nav class="nav-links" aria-label="Primary navigation">
          <RouterLink to="/">Home</RouterLink>
          <RouterLink to="/articles">Article List</RouterLink>
          <RouterLink to="/categories">Categories/Tags</RouterLink>
          <RouterLink to="/comments">Comment Board</RouterLink>
          <RouterLink to="/about">About Me</RouterLink>
          <RouterLink to="/admin/login" title="Admin login">
            <el-icon><LogIn /></el-icon>
          </RouterLink>
        </nav>
      </div>
    </header>
    <main class="main-content">
      <slot />
    </main>
    <footer class="site-footer">
      <div class="site-footer__inner">
        <span>(c) 2026 Photography Journal</span>
        <span>Light, weather, and field notes</span>
      </div>
    </footer>
  </div>
</template>
