import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
  baseURL: 'http://localhost:8082',
  timeout: 500000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 不需要认证的接口
const publicApis = ['/ai/auth/login', '/ai/auth/register']

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    console.log('Request config:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    })
    
    // 如果是公开接口，不添加 token
    if (config.url && publicApis.includes(config.url)) {
      console.log('Public API request:', config.url)
      return config
    }

    // 从 localStorage 获取 token
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    console.error('Request error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response: AxiosResponse) => {
    // 如果是文件下载请求（responseType为blob），返回完整的response对象
    if (response.config.responseType === 'blob') {
      return response
    }
    return response.data
  },
  (error) => {
    console.error('Response error:', error)
    const responseData = error.response?.data
    const errorCode = responseData?.errorCode
    const message = responseData?.message || '请求失败'
    
    // 统一显示后端返回的错误信息
    ElMessage.error(message)
    
    // 如果是认证相关错误或 token 过期，清除token并跳转到登录页
    if (errorCode?.startsWith('AUTH_') || error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('username')
      localStorage.removeItem('roles')
      // 使用 router 进行导航，而不是直接修改 location
      router.push('/login')
    }
    
    return Promise.reject(error)
  }
)

// 封装 GET 请求
const get = <T>(url: string, config?: AxiosRequestConfig): Promise<T> => {
  return service.get(url, config)
}

// 封装 POST 请求
const post = <T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
  return service.post(url, data, config)
}

// 封装 PUT 请求
const put = <T>(url: string, data?: any, config?: AxiosRequestConfig): Promise<T> => {
  return service.put(url, data, config)
}

// 封装 DELETE 请求
const del = <T>(url: string, config?: AxiosRequestConfig): Promise<T> => {
  return service.delete(url, config)
}

// 验证 token 是否有效
export const validateToken = async () => {
  const token = localStorage.getItem('token')
  if (!token) return false

  try {
    // 尝试发送一个请求来验证 token
    await service.get('/ai/auth/validate')
    return true
  } catch (error) {
    // 如果请求失败，说明 token 无效
    return false
  }
}

export default {
  get,
  post,
  put,
  delete: del
} 