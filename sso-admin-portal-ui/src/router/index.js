import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/',
    name: 'MainLayout',
    component: () => import('@/views/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '仪表盘', icon: 'Odometer' }
      },
      {
        path: 'user',
        name: 'UserManage',
        component: () => import('@/views/UserManage.vue'),
        meta: { title: '用户管理', icon: 'User' }
      },
      {
        path: 'tenant',
        name: 'TenantManage',
        component: () => import('@/views/TenantManage.vue'),
        meta: { title: '租户管理', icon: 'OfficeBuilding' }
      },
      {
        path: 'role',
        name: 'RoleManage',
        component: () => import('@/views/RoleManage.vue'),
        meta: { title: '角色管理', icon: 'UserFilled' }
      },
      {
        path: 'permission',
        name: 'PermissionManage',
        component: () => import('@/views/PermissionManage.vue'),
        meta: { title: '权限管理', icon: 'Lock' }
      },
      {
        path: 'audit',
        name: 'AuditLog',
        component: () => import('@/views/AuditLog.vue'),
        meta: { title: '审计日志', icon: 'Document' }
      },
      {
        path: 'app',
        name: 'AppManage',
        component: () => import('@/views/AppManage.vue'),
        meta: { title: '应用管理', icon: 'Connection' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - SSO管理后台` : 'SSO管理后台'
  const token = localStorage.getItem('sso_token')
  if (to.path === '/login') {
    next()
  } else {
    if (token) {
      next()
    } else {
      next('/login')
    }
  }
})

export default router
