import request, { unwrap } from './request'

export const publicApi = {
  articles(params = {}) {
    return request.get('/articles', { params }).then(unwrap)
  },
  article(slug) {
    return request.get(`/articles/${slug}`).then(unwrap)
  },
  categories() {
    return request.get('/categories').then(unwrap)
  },
  tags() {
    return request.get('/tags').then(unwrap)
  },
  comments(params = {}) {
    return request.get('/comments', { params }).then(unwrap)
  },
  createComment(payload) {
    return request.post('/comments', payload).then(unwrap)
  },
  profile() {
    return request.get('/profile').then(unwrap)
  }
}
