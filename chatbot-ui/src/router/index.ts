import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/store/auth'
import type { UserRole } from '@/api/types'
import { validateToken } from '@/utils/request'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: () => import('@/views/LoginView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/register',
      name: 'register',
      component: () => import('@/views/RegisterView.vue'),
      meta: { requiresAuth: false }
    },
    {
      path: '/',
      component: () => import('@/layouts/DefaultLayout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: '/chat'
        },
        {
          path: 'chat',
          name: 'chat',
          component: () => import('@/views/ChatView.vue')
        },
        {
          path: 'knowledge',
          name: 'knowledge',
          component: () => import('@/views/KnowledgeManagement.vue'),
          meta: { roles: ['ROLE_ADMIN', 'ROLE_KNOWLEDGEMANAGER'] as UserRole[] }
        },
        {
          path: 'users',
          name: 'users',
          component: () => import('@/views/UserManagement.vue'),
          meta: { roles: ['ROLE_ADMIN'] as UserRole[] }
        }
      ]
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiredRoles = to.matched.some(record => record.meta.roles) 
    ? to.matched.find(record => record.meta.roles)?.meta.roles as UserRole[]
    : null

  // 如果路由需要认证
  if (requiresAuth) {
    // 如果没有 token，直接跳转到登录页
    if (!authStore.token) {
      next('/login')
      return
    }

    // 验证 token 是否有效
    const isValid = await validateToken()
    if (!isValid) {
      // token 无效，清除用户信息并跳转到登录页
      authStore.clearToken()
      next('/login')
      return
    }
  }

  // 如果用户已登录但访问登录/注册页，重定向到聊天页
  if ((to.path === '/login' || to.path === '/register') && authStore.token) {
    next('/chat')
  } 
  // 如果路由需要特定角色但用户没有，重定向到聊天页
  else if (requiredRoles && !authStore.checkAnyRole(requiredRoles)) {
    next('/chat')
  } 
  // 其他情况正常导航
  else {
    next()
  }
})

export default router 