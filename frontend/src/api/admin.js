import request, { unwrap } from './request'

export const adminApi = {
  login(payload) {
    return request.post('/admin/auth/login', payload).then(unwrap)
  },
  dashboard() {
    return request.get('/admin/dashboard').then(unwrap)
  },
  articles(params = {}) {
    return request.get('/admin/articles', { params }).then(unwrap)
  },
  article(id) {
    return request.get(`/admin/articles/${id}`).then(unwrap)
  },
  createArticle(payload) {
    return request.post('/admin/articles', payload).then(unwrap)
  },
  updateArticle(id, payload) {
    return request.put(`/admin/articles/${id}`, payload).then(unwrap)
  },
  deleteArticle(id) {
    return request.delete(`/admin/articles/${id}`).then(unwrap)
  },
  categories() {
    return request.get('/admin/categories').then(unwrap)
  },
  createCategory(payload) {
    return request.post('/admin/categories', payload).then(unwrap)
  },
  updateCategory(id, payload) {
    return request.put(`/admin/categories/${id}`, payload).then(unwrap)
  },
  deleteCategory(id) {
    return request.delete(`/admin/categories/${id}`).then(unwrap)
  },
  tags() {
    return request.get('/admin/tags').then(unwrap)
  },
  createTag(payload) {
    return request.post('/admin/tags', payload).then(unwrap)
  },
  updateTag(id, payload) {
    return request.put(`/admin/tags/${id}`, payload).then(unwrap)
  },
  deleteTag(id) {
    return request.delete(`/admin/tags/${id}`).then(unwrap)
  },
  comments(params = {}) {
    return request.get('/admin/comments', { params }).then(unwrap)
  },
  updateComment(id, payload) {
    return request.patch(`/admin/comments/${id}`, payload).then(unwrap)
  },
  deleteComment(id) {
    return request.delete(`/admin/comments/${id}`).then(unwrap)
  },
  images() {
    return request.get('/admin/images').then(unwrap)
  },
  uploadImage(file) {
    const body = new FormData()
    body.append('file', file)
    return request.post('/admin/images', body, {
      headers: { 'Content-Type': 'multipart/form-data' }
    }).then(unwrap)
  },
  deleteImage(id) {
    return request.delete(`/admin/images/${id}`).then(unwrap)
  }
}
