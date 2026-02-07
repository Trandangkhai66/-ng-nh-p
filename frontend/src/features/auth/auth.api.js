const API = import.meta.env.VITE_API_URL || ''

export const googleLogin = () => {
  window.location.href = `${API || ''}/oauth2/authorization/google`
}

export const logout = () => {
  localStorage.removeItem('token')
  window.location.href = '/login'
}
