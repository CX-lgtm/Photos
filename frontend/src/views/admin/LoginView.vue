<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Lock, User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { adminApi } from '../../api/admin'
import { setSession } from '../../utils/auth'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: ''
})

async function submit() {
  if (!form.username || !form.password) {
    ElMessage.warning('Username and password are required')
    return
  }
  loading.value = true
  try {
    const session = await adminApi.login(form)
    setSession(session)
    router.push(route.query.redirect || '/admin')
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <main class="login-page">
    <section class="login-panel">
      <div class="login-panel__body">
        <div class="admin-title">
          <h1>Admin Login</h1>
          <p>Private workspace for the journal.</p>
        </div>
        <el-form label-position="top" @submit.prevent>
          <el-form-item label="Username">
            <el-input v-model="form.username" :prefix-icon="User" autocomplete="username" />
          </el-form-item>
          <el-form-item label="Password">
            <el-input
              v-model="form.password"
              :prefix-icon="Lock"
              type="password"
              autocomplete="current-password"
              show-password
              @keyup.enter="submit"
            />
          </el-form-item>
          <el-button type="primary" size="large" style="width: 100%;" :loading="loading" @click="submit">
            Login
          </el-button>
        </el-form>
      </div>
    </section>
  </main>
</template>
