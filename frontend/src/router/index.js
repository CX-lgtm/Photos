import { createRouter, createWebHistory } from 'vue-router'
import { getToken } from '../utils/auth'

const routes = [
  { path: '/', name: 'home', component: () => import('../views/HomeView.vue') },
  { path: '/articles', name: 'articles', component: () => import('../views/ArticleListView.vue') },
  { path: '/articles/:slug', name: 'article-detail', component: () => import('../views/ArticleDetailView.vue') },
  { path: '/categories', name: 'categories', component: () => import('../views/CategoriesTagsView.vue') },
  { path: '/comments', name: 'comments', component: () => import('../views/CommentBoardView.vue') },
  { path: '/about', name: 'about', component: () => import('../views/AboutView.vue') },
  { path: '/admin/login', name: 'admin-login', component: () => import('../views/admin/LoginView.vue') },
  { path: '/admin', name: 'admin-dashboard', meta: { admin: true }, component: () => import('../views/admin/DashboardView.vue') },
  { path: '/admin/articles', name: 'admin-articles', meta: { admin: true }, component: () => import('../views/admin/ArticleManageView.vue') },
  { path: '/admin/articles/new', name: 'admin-article-new', meta: { admin: true }, component: () => import('../views/admin/ArticleEditorView.vue') },
  { path: '/admin/articles/:id/edit', name: 'admin-article-edit', meta: { admin: true }, component: () => import('../views/admin/ArticleEditorView.vue') },
  { path: '/admin/categories', name: 'admin-categories', meta: { admin: true }, component: () => import('../views/admin/CategoryManageView.vue') },
  { path: '/admin/tags', name: 'admin-tags', meta: { admin: true }, component: () => import('../views/admin/TagManageView.vue') },
  { path: '/admin/comments', name: 'admin-comments', meta: { admin: true }, component: () => import('../views/admin/CommentManageView.vue') },
  { path: '/admin/images', name: 'admin-images', meta: { admin: true }, component: () => import('../views/admin/ImageManageView.vue') }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to) => {
  if (to.meta.admin && !getToken()) {
    return { name: 'admin-login', query: { redirect: to.fullPath } }
  }
  if (to.name === 'admin-login' && getToken()) {
    return { name: 'admin-dashboard' }
  }
  return true
})

export default router
