import axios from 'axios'
import { ElMessage } from 'element-plus'
import { clearSession, getToken } from '../utils/auth'

const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  timeout: 15000
})

request.interceptors.request.use((config) => {
  const token = getToken()
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  return config
})

request.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status
    const message = error.response?.data?.message || error.message || 'Request failed'
    if (status === 401) {
      clearSession()
      if (!location.pathname.includes('/admin/login')) {
        location.href = `/admin/login?redirect=${encodeURIComponent(location.pathname)}`
      }
    } else {
      ElMessage.error(message)
    }
    return Promise.reject(error)
  }
)

export function unwrap(response) {
  return response.data.data
}

export default request
