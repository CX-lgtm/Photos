import request, { unwrap } from './request'

const silent = { silentError: true }

export const publicApi = {
  articles(params = {}) {
    return request.get('/articles', { params, ...silent }).then(unwrap)
  },
  article(slug) {
    return request.get(`/articles/${slug}`, silent).then(unwrap)
  },
  categories() {
    return request.get('/categories', silent).then(unwrap)
  },
  tags() {
    return request.get('/tags', silent).then(unwrap)
  },
  comments(params = {}) {
    return request.get('/comments', { params, ...silent }).then(unwrap)
  },
  createComment(payload) {
    return request.post('/comments', payload).then(unwrap)
  },
  profile() {
    return request.get('/profile', silent).then(unwrap)
  }
}
