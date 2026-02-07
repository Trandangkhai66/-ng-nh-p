import api from '../../services/api'

export const getUsers = () => api.get('/api/users').then((r) => r.data?.data ?? [])
export const getUserById = (id) => api.get(`/api/users/${id}`).then((r) => r.data?.data)
export const updateUser = (id, body) => api.put(`/api/users/${id}`, body).then((r) => r.data?.data)
