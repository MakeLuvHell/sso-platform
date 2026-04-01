import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('sso_token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('sso_userInfo') || '{}'))

  function login(username, password) {
    return new Promise((resolve, reject) => {
      // 模拟登录验证
      setTimeout(() => {
        if (username === 'admin' && password === 'admin123') {
          const mockToken = 'mock-jwt-token-' + Date.now()
          const mockUser = {
            id: 1,
            username: 'admin',
            name: '系统管理员',
            email: 'admin@sso.com',
            avatar: '',
            roles: ['超级管理员']
          }
          token.value = mockToken
          userInfo.value = mockUser
          localStorage.setItem('sso_token', mockToken)
          localStorage.setItem('sso_userInfo', JSON.stringify(mockUser))
          resolve(mockUser)
        } else {
          reject(new Error('用户名或密码错误'))
        }
      }, 500)
    })
  }

  function logout() {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('sso_token')
    localStorage.removeItem('sso_userInfo')
  }

  return {
    token,
    userInfo,
    login,
    logout
  }
})
