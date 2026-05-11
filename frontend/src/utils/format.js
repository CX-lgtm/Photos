import dayjs from 'dayjs'

export function formatDate(value) {
  return value ? dayjs(value).format('YYYY-MM-DD') : '-'
}

export function formatDateTime(value) {
  return value ? dayjs(value).format('YYYY-MM-DD HH:mm') : '-'
}

export function absoluteAssetUrl(url) {
  if (!url) return ''
  if (/^https?:\/\//i.test(url)) return url
  const apiBase = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api'
  const origin = apiBase.replace(/\/api\/?$/, '')
  return `${origin}${url.startsWith('/') ? url : `/${url}`}`
}
