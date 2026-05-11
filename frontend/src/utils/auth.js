const TOKEN_KEY = 'photography_blog_admin_token'
const USER_KEY = 'photography_blog_admin_user'

export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

export function setSession(session) {
  localStorage.setItem(TOKEN_KEY, session.token)
  localStorage.setItem(USER_KEY, JSON.stringify({
    username: session.username,
    displayName: session.displayName
  }))
}

export function getUser() {
  const raw = localStorage.getItem(USER_KEY)
  return raw ? JSON.parse(raw) : null
}

export function clearSession() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}
