import request from '@/utils/request'
import type { PageResponse } from './types'

export interface User {
  id?: number
  username: string
  email: string
  password?: string
  roles: string[]
}

export const userApi = {
  getAll: (page: number, size: number) => {
    return request.get<PageResponse<User>>('/api/users', {
      params: { page, size }
    })
  },

  getById: (id: number) => {
    return request.get<User>(`/api/users/${id}`)
  },

  create: (user: User) => {
    return request.post<User>('/api/users', user)
  },

  update: (id: number, user: User) => {
    return request.put<User>(`/api/users/${id}`, user)
  },

  delete: (id: number) => {
    return request.delete(`/api/users/${id}`)
  }
} 