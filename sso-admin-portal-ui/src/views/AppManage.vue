<template>
  <div class="app-manage">
    <!-- 搜索栏 -->
    <el-card class="search-card" shadow="never">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="应用名称">
          <el-input v-model="searchForm.appName" placeholder="请输入应用名称" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="应用类型">
          <el-select v-model="searchForm.appType" placeholder="全部" clearable style="width: 150px">
            <el-option label="Web应用" :value="1" />
            <el-option label="移动应用" :value="2" />
            <el-option label="SPA单页应用" :value="3" />
            <el-option label="服务端应用" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部" clearable style="width: 120px">
            <el-option label="启用" :value="0" />
            <el-option label="禁用" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
          <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 表格区域 -->
    <el-card class="table-card" shadow="never">
      <template #header>
        <div class="card-header">
          <span class="title">应用列表</span>
          <el-button type="primary" :icon="Plus" @click="handleAdd">注册应用</el-button>
        </div>
      </template>

      <el-table :data="tableData" border stripe style="width: 100%" v-loading="loading">
        <el-table-column prop="appName" label="应用名称" min-width="140">
          <template #default="{ row }">
            <div style="display: flex; align-items: center; gap: 8px;">
              <el-avatar v-if="row.logoUrl" :size="28" :src="row.logoUrl" />
              <el-avatar v-else :size="28" style="background: #409EFF; color: #fff; font-size: 12px;">
                {{ row.appName.charAt(0) }}
              </el-avatar>
              <span style="font-weight: 500;">{{ row.appName }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="appCode" label="应用编码" width="140" />
        <el-table-column prop="clientIdShow" label="Client ID" width="200" />
        <el-table-column prop="appType" label="应用类型" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="appTypeTag(row.appType)" size="small">{{ appTypeLabel(row.appType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scopes" label="授权范围" min-width="160" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-switch
              :model-value="row.status === 0"
              @change="(val) => handleStatusChange(row, val)"
              inline-prompt
              active-text="启"
              inactive-text="禁"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="320" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link :icon="View" @click="handleView(row)">详情</el-button>
            <el-button type="primary" link :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link :icon="Key" @click="handleResetSecret(row)">重置密钥</el-button>
            <el-button type="success" link :icon="Connection" @click="handleSync(row)">同步</el-button>
            <el-button type="danger" link :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchData"
          @current-change="fetchData"
        />
      </div>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogType === 'add' ? '注册新应用' : '编辑应用'"
      width="680px"
      :close-on-click-modal="false"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="130px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用名称" prop="appName">
              <el-input v-model="form.appName" placeholder="请输入应用名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用编码" prop="appCode">
              <el-input v-model="form.appCode" placeholder="请输入应用编码（英文）" :disabled="dialogType === 'edit'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="应用类型" prop="appType">
              <el-select v-model="form.appType" placeholder="请选择" style="width: 100%">
                <el-option label="Web应用" :value="1" />
                <el-option label="移动应用" :value="2" />
                <el-option label="SPA单页应用" :value="3" />
                <el-option label="服务端应用" :value="4" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="应用Logo">
              <el-input v-model="form.logoUrl" placeholder="Logo URL（可选）" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="应用描述">
          <el-input v-model="form.description" type="textarea" :rows="2" placeholder="请输入应用描述" />
        </el-form-item>
        <el-form-item label="应用首页">
          <el-input v-model="form.homeUrl" placeholder="https://your-app.example.com" />
        </el-form-item>
        <el-form-item label="回调地址" prop="redirectUris">
          <el-input
            v-model="form.redirectUris"
            type="textarea"
            :rows="3"
            placeholder="OAuth2 授权回调地址，多个地址用逗号分隔&#10;例如：https://your-app.com/login/oauth2/code/sso"
          />
          <div class="form-tip">用户授权后，SSO 会将授权码重定向到此地址</div>
        </el-form-item>
        <el-form-item label="登出回调地址">
          <el-input
            v-model="form.postLogoutRedirectUris"
            type="textarea"
            :rows="2"
            placeholder="用户登出后的重定向地址，多个用逗号分隔"
          />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授权范围">
              <el-input v-model="form.scopes" placeholder="openid,profile,email" />
              <div class="form-tip">多个 scope 用逗号分隔</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="授权类型">
              <el-select v-model="form.grantTypes" multiple placeholder="请选择" style="width: 100%">
                <el-option label="授权码模式" value="authorization_code" />
                <el-option label="刷新令牌" value="refresh_token" />
                <el-option label="客户端凭证" value="client_credentials" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Token有效期">
              <el-input-number v-model="form.accessTokenValidity" :min="300" :max="86400" :step="3600" style="width: 100%" />
              <div class="form-tip">访问令牌有效期（秒），默认 2 小时</div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="刷新Token有效期">
              <el-input-number v-model="form.refreshTokenValidity" :min="3600" :max="2592000" :step="86400" style="width: 100%" />
              <div class="form-tip">刷新令牌有效期（秒），默认 30 天</div>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>

    <!-- 应用详情对话框 -->
    <el-dialog v-model="detailVisible" title="应用详情" width="700px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="应用名称">{{ currentApp.appName }}</el-descriptions-item>
        <el-descriptions-item label="应用编码">{{ currentApp.appCode }}</el-descriptions-item>
        <el-descriptions-item label="应用类型">
          <el-tag :type="appTypeTag(currentApp.appType)" size="small">{{ appTypeLabel(currentApp.appType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentApp.status === 0 ? 'success' : 'danger'" size="small">
            {{ currentApp.status === 0 ? '启用' : '禁用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Client ID" :span="2">
          <div style="display: flex; align-items: center; gap: 8px;">
            <code style="background: #f5f7fa; padding: 4px 8px; border-radius: 4px; font-size: 13px;">{{ currentApp.clientId }}</code>
            <el-button type="primary" link size="small" :icon="CopyDocument" @click="copyText(currentApp.clientId)">复制</el-button>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="Client Secret" :span="2">
          <div style="display: flex; align-items: center; gap: 8px;">
            <code style="background: #f5f7fa; padding: 4px 8px; border-radius: 4px; font-size: 13px;">
              {{ showSecret ? currentApp.clientSecret : '••••••••••••••••' }}
            </code>
            <el-button type="primary" link size="small" :icon="showSecret ? Hide : View" @click="showSecret = !showSecret">
              {{ showSecret ? '隐藏' : '显示' }}
            </el-button>
            <el-button type="primary" link size="small" :icon="CopyDocument" @click="copyText(currentApp.clientSecret)">复制</el-button>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="应用描述" :span="2">{{ currentApp.description || '-' }}</el-descriptions-item>
        <el-descriptions-item label="应用首页" :span="2">
          <a v-if="currentApp.homeUrl" :href="currentApp.homeUrl" target="_blank" style="color: #409EFF;">{{ currentApp.homeUrl }}</a>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="回调地址" :span="2">
          <div v-if="currentApp.redirectUris">
            <el-tag v-for="uri in currentApp.redirectUris.split(',')" :key="uri" size="small" style="margin: 2px 4px 2px 0;">
              {{ uri }}
            </el-tag>
          </div>
          <span v-else>-</span>
        </el-descriptions-item>
        <el-descriptions-item label="授权范围" :span="2">
          <el-tag v-for="scope in (currentApp.scopes || '').split(',')" :key="scope" type="info" size="small" style="margin: 2px 4px 2px 0;">
            {{ scope }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="授权类型" :span="2">
          <el-tag v-for="gt in (currentApp.grantTypes || '').split(',')" :key="gt" size="small" style="margin: 2px 4px 2px 0;">
            {{ gt }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="Token有效期">{{ formatSeconds(currentApp.accessTokenValidity) }}</el-descriptions-item>
        <el-descriptions-item label="刷新Token有效期">{{ formatSeconds(currentApp.refreshTokenValidity) }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ currentApp.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentApp.updateTime }}</el-descriptions-item>
      </el-descriptions>

      <!-- 接入指南 -->
      <el-divider content-position="left">快速接入指南</el-divider>
      <div class="guide-section">
        <el-alert type="info" :closable="false" show-icon style="margin-bottom: 12px;">
          <template #title>将以下配置添加到您的应用中，即可接入 SSO 单点登录</template>
        </el-alert>
        <el-tabs>
          <el-tab-pane label="Spring Boot">
            <pre class="code-block">spring:
  security:
    oauth2:
      client:
        provider:
          sso:
            issuer-uri: http://localhost:9000
            authorization-uri: http://localhost:9000/auth/authorize
            token-uri: http://localhost:9000/auth/token
            user-info-uri: http://localhost:9000/auth/userinfo
            jwk-set-uri: http://localhost:9000/auth/jwks
        registration:
          {{ currentApp.appCode || 'your-app' }}:
            client-id: {{ currentApp.clientId || 'your-client-id' }}
            client-secret: {{ currentApp.clientSecret || 'your-client-secret' }}
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/sso"
            scope:
              - openid
              - profile</pre>
          </el-tab-pane>
          <el-tab-pane label="通用 OIDC">
            <pre class="code-block">Authorization Endpoint: http://localhost:9000/auth/authorize
Token Endpoint:         http://localhost:9000/auth/token
Userinfo Endpoint:      http://localhost:9000/auth/userinfo
JWKS Endpoint:          http://localhost:9000/auth/jwks

Client ID:     {{ currentApp.clientId || 'your-client-id' }}
Client Secret: {{ currentApp.clientSecret || 'your-client-secret' }}
Redirect URI:  {{ (currentApp.redirectUris || '').split(',')[0] || 'https://your-app.com/callback' }}

// 授权码模式授权URL示例:
http://localhost:9000/auth/authorize?
  response_type=code&
  client_id={{ currentApp.clientId || 'your-client-id' }}&
  redirect_uri={{ encodeURIComponent((currentApp.redirectUris || '').split(',')[0] || 'https://your-app.com/callback') }}&
  scope=openid profile&
  state=random_state_string</pre>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, View, Edit, Delete, Key, Connection, CopyDocument } from '@element-plus/icons-vue'

const loading = ref(false)
const submitLoading = ref(false)
const dialogVisible = ref(false)
const detailVisible = ref(false)
const showSecret = ref(false)
const dialogType = ref('add')
const formRef = ref(null)
const tableData = ref([])
const currentApp = ref({})

const searchForm = reactive({
  appName: '',
  appType: null,
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const form = reactive({
  appName: '',
  appCode: '',
  appType: 1,
  description: '',
  logoUrl: '',
  homeUrl: '',
  redirectUris: '',
  postLogoutRedirectUris: '',
  scopes: 'openid,profile',
  grantTypes: ['authorization_code', 'refresh_token'],
  accessTokenValidity: 7200,
  refreshTokenValidity: 2592000
})

const formRules = {
  appName: [{ required: true, message: '请输入应用名称', trigger: 'blur' }],
  appCode: [
    { required: true, message: '请输入应用编码', trigger: 'blur' },
    { pattern: /^[a-zA-Z][a-zA-Z0-9_-]*$/, message: '编码须以字母开头，只允许字母、数字、下划线和横线', trigger: 'blur' }
  ],
  redirectUris: [{ required: true, message: '请输入回调地址', trigger: 'blur' }]
}

// 模拟数据
const mockApps = [
  { id: 1, appName: 'OA办公系统', appCode: 'oa-system', clientId: 'app_a1b2c3d4e5f67890', clientSecret: 'sk_x9y8z7w6v5u4t3s2r1q0p', clientIdShow: 'app_a1b2****7890', appType: 1, description: '公司内部OA办公系统', logoUrl: '', homeUrl: 'https://oa.company.com', redirectUris: 'https://oa.company.com/login/oauth2/code/sso', postLogoutRedirectUris: 'https://oa.company.com', scopes: 'openid,profile,email', grantTypes: 'authorization_code,refresh_token', accessTokenValidity: 7200, refreshTokenValidity: 2592000, status: 0, createTime: '2026-01-10 10:00:00', updateTime: '2026-03-15 14:30:00' },
  { id: 2, appName: 'CRM客户管理', appCode: 'crm-system', clientId: 'app_m1n2o3p4q5r6s7t8', clientSecret: 'sk_k1j2i3h4g5f6d7e8c9b0', clientIdShow: 'app_m1n2****s7t8', appType: 1, description: '客户关系管理系统', logoUrl: '', homeUrl: 'https://crm.company.com', redirectUris: 'https://crm.company.com/login/oauth2/code/sso', postLogoutRedirectUris: 'https://crm.company.com', scopes: 'openid,profile', grantTypes: 'authorization_code,refresh_token', accessTokenValidity: 7200, refreshTokenValidity: 2592000, status: 0, createTime: '2026-01-20 14:30:00', updateTime: '2026-03-20 09:15:00' },
  { id: 3, appName: 'HR人力资源', appCode: 'hr-system', clientId: 'app_u1v2w3x4y5z6a7b8', clientSecret: 'sk_a1s2d3f4g5h6j7k8l9z', clientIdShow: 'app_u1v2****a7b8', appType: 1, description: '人力资源管理系统', logoUrl: '', homeUrl: 'https://hr.company.com', redirectUris: 'https://hr.company.com/login/oauth2/code/sso', postLogoutRedirectUris: 'https://hr.company.com', scopes: 'openid,profile,email,phone', grantTypes: 'authorization_code,refresh_token', accessTokenValidity: 3600, refreshTokenValidity: 604800, status: 0, createTime: '2026-02-01 09:00:00', updateTime: '2026-03-18 16:45:00' },
  { id: 4, appName: '移动端APP', appCode: 'mobile-app', clientId: 'app_c3d4e5f6g7h8i9j0', clientSecret: 'sk_q1w2e3r4t5y6u7i8o9p', clientIdShow: 'app_c3d4****i9j0', appType: 2, description: '移动端办公应用', logoUrl: '', homeUrl: '', redirectUris: 'myapp://oauth2/callback,https://mobile.company.com/callback', postLogoutRedirectUris: '', scopes: 'openid,profile', grantTypes: 'authorization_code,refresh_token', accessTokenValidity: 7200, refreshTokenValidity: 2592000, status: 0, createTime: '2026-02-15 11:20:00', updateTime: '2026-03-10 10:00:00' },
  { id: 5, appName: '数据大屏', appCode: 'data-screen', clientId: 'app_k1l2m3n4o5p6q7r8', clientSecret: 'sk_z1x2c3v4b5n6m7k8j9h', clientIdShow: 'app_k1l2****q7r8', appType: 3, description: '数据可视化大屏展示', logoUrl: '', homeUrl: 'https://screen.company.com', redirectUris: 'https://screen.company.com/oauth2/callback', postLogoutRedirectUris: '', scopes: 'openid', grantTypes: 'authorization_code', accessTokenValidity: 3600, refreshTokenValidity: 0, status: 0, createTime: '2026-03-01 15:40:00', updateTime: '2026-03-12 08:30:00' },
  { id: 6, appName: 'API网关服务', appCode: 'api-gateway', clientId: 'app_w1x2y3z4a5b6c7d8', clientSecret: 'sk_g1f2d3s4a5q6w7e8r9t', clientIdShow: 'app_w1x2****c7d8', appType: 4, description: '内部API网关，使用客户端凭证模式', logoUrl: '', homeUrl: '', redirectUris: '', postLogoutRedirectUris: '', scopes: 'openid', grantTypes: 'client_credentials', accessTokenValidity: 7200, refreshTokenValidity: 0, status: 0, createTime: '2026-03-05 10:00:00', updateTime: '2026-03-20 11:20:00' },
  { id: 7, appName: '测试应用', appCode: 'test-app', clientId: 'app_e9f0g1h2i3j4k5l6', clientSecret: 'sk_p1o2i3u4y5t6r7e8w9q', clientIdShow: 'app_e9f0****k5l6', appType: 1, description: '用于测试SSO接入流程', logoUrl: '', homeUrl: 'http://localhost:3000', redirectUris: 'http://localhost:3000/login/oauth2/code/sso', postLogoutRedirectUris: '', scopes: 'openid,profile,email', grantTypes: 'authorization_code,refresh_token', accessTokenValidity: 7200, refreshTokenValidity: 2592000, status: 1, createTime: '2026-03-10 08:00:00', updateTime: '2026-03-25 17:00:00' }
]

const appTypeLabel = (type) => {
  const map = { 1: 'Web应用', 2: '移动应用', 3: 'SPA应用', 4: '服务端应用' }
  return map[type] || '未知'
}

const appTypeTag = (type) => {
  const map = { 1: '', 2: 'success', 3: 'warning', 4: 'info' }
  return map[type] || 'info'
}

const formatSeconds = (seconds) => {
  if (!seconds) return '不适用'
  if (seconds < 60) return `${seconds} 秒`
  if (seconds < 3600) return `${Math.floor(seconds / 60)} 分钟`
  if (seconds < 86400) return `${Math.floor(seconds / 3600)} 小时`
  return `${Math.floor(seconds / 86400)} 天`
}

const copyText = (text) => {
  navigator.clipboard.writeText(text).then(() => {
    ElMessage.success('已复制到剪贴板')
  }).catch(() => {
    ElMessage.error('复制失败')
  })
}

const fetchData = () => {
  loading.value = true
  setTimeout(() => {
    let filtered = [...mockApps]
    if (searchForm.appName) {
      filtered = filtered.filter(a => a.appName.includes(searchForm.appName))
    }
    if (searchForm.appType) {
      filtered = filtered.filter(a => a.appType === searchForm.appType)
    }
    if (searchForm.status !== null && searchForm.status !== undefined) {
      filtered = filtered.filter(a => a.status === searchForm.status)
    }
    pagination.total = filtered.length
    const start = (pagination.page - 1) * pagination.size
    tableData.value = filtered.slice(start, start + pagination.size)
    loading.value = false
  }, 300)
}

const handleSearch = () => {
  pagination.page = 1
  fetchData()
}

const handleReset = () => {
  searchForm.appName = ''
  searchForm.appType = null
  searchForm.status = null
  pagination.page = 1
  fetchData()
}

const resetForm = () => {
  form.appName = ''
  form.appCode = ''
  form.appType = 1
  form.description = ''
  form.logoUrl = ''
  form.homeUrl = ''
  form.redirectUris = ''
  form.postLogoutRedirectUris = ''
  form.scopes = 'openid,profile'
  form.grantTypes = ['authorization_code', 'refresh_token']
  form.accessTokenValidity = 7200
  form.refreshTokenValidity = 2592000
}

const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogType.value = 'edit'
  Object.assign(form, {
    appName: row.appName,
    appCode: row.appCode,
    appType: row.appType,
    description: row.description,
    logoUrl: row.logoUrl,
    homeUrl: row.homeUrl,
    redirectUris: row.redirectUris,
    postLogoutRedirectUris: row.postLogoutRedirectUris,
    scopes: row.scopes,
    grantTypes: row.grantTypes.split(','),
    accessTokenValidity: row.accessTokenValidity,
    refreshTokenValidity: row.refreshTokenValidity
  })
  dialogVisible.value = true
}

const handleView = (row) => {
  currentApp.value = { ...row }
  showSecret.value = false
  detailVisible.value = true
}

const handleStatusChange = (row, val) => {
  const newStatus = val ? 0 : 1
  const action = newStatus === 0 ? '启用' : '禁用'
  ElMessageBox.confirm(`确定要${action}应用「${row.appName}」吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    row.status = newStatus
    ElMessage.success(`已${action}应用「${row.appName}」`)
  }).catch(() => {})
}

const handleResetSecret = (row) => {
  ElMessageBox.confirm(
    `重置密钥后，旧密钥将立即失效！应用「${row.appName}」需要更新配置。\n\n确定要重置吗？`,
    '⚠️ 重置 Client Secret',
    {
      confirmButtonText: '确定重置',
      cancelButtonText: '取消',
      type: 'warning',
      confirmButtonClass: 'el-button--danger'
    }
  ).then(() => {
    const chars = 'abcdefghijklmnopqrstuvwxyz0123456789'
    let newSecret = 'sk_'
    for (let i = 0; i < 32; i++) {
      newSecret += chars.charAt(Math.floor(Math.random() * chars.length))
    }
    row.clientSecret = newSecret
    row.clientIdShow = row.clientId.substring(0, 8) + '****' + row.clientId.substring(row.clientId.length - 4)
    ElMessage.success('Client Secret 已重置，请及时更新应用配置')
  }).catch(() => {})
}

const handleSync = (row) => {
  ElMessage.success(`应用「${row.appName}」已同步到授权服务器`)
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除应用「${row.appName}」吗？删除后不可恢复。`, '删除确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const idx = mockApps.findIndex(a => a.id === row.id)
    if (idx > -1) mockApps.splice(idx, 1)
    ElMessage.success('删除成功')
    fetchData()
  }).catch(() => {})
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) return
    submitLoading.value = true
    setTimeout(() => {
      if (dialogType.value === 'add') {
        const newId = Math.max(...mockApps.map(a => a.id)) + 1
        const clientId = 'app_' + Array.from({ length: 16 }, () => 'abcdefghijklmnopqrstuvwxyz0123456789'.charAt(Math.floor(Math.random() * 36))).join('')
        const clientSecret = 'sk_' + Array.from({ length: 32 }, () => 'abcdefghijklmnopqrstuvwxyz0123456789'.charAt(Math.floor(Math.random() * 36))).join('')
        mockApps.unshift({
          id: newId,
          appName: form.appName,
          appCode: form.appCode,
          clientId,
          clientSecret,
          clientIdShow: clientId.substring(0, 8) + '****' + clientId.substring(clientId.length - 4),
          appType: form.appType,
          description: form.description,
          logoUrl: form.logoUrl,
          homeUrl: form.homeUrl,
          redirectUris: form.redirectUris,
          postLogoutRedirectUris: form.postLogoutRedirectUris,
          scopes: form.scopes,
          grantTypes: form.grantTypes.join(','),
          accessTokenValidity: form.accessTokenValidity,
          refreshTokenValidity: form.refreshTokenValidity,
          status: 0,
          createTime: new Date().toISOString().replace('T', ' ').substring(0, 19),
          updateTime: new Date().toISOString().replace('T', ' ').substring(0, 19)
        })
        ElMessage.success('应用注册成功')
      } else {
        const app = mockApps.find(a => a.id === currentApp.value.id)
        if (app) {
          Object.assign(app, {
            appName: form.appName,
            appType: form.appType,
            description: form.description,
            logoUrl: form.logoUrl,
            homeUrl: form.homeUrl,
            redirectUris: form.redirectUris,
            postLogoutRedirectUris: form.postLogoutRedirectUris,
            scopes: form.scopes,
            grantTypes: form.grantTypes.join(','),
            accessTokenValidity: form.accessTokenValidity,
            refreshTokenValidity: form.refreshTokenValidity,
            updateTime: new Date().toISOString().replace('T', ' ').substring(0, 19)
          })
        }
        ElMessage.success('应用更新成功')
      }
      dialogVisible.value = false
      submitLoading.value = false
      fetchData()
    }, 500)
  })
}

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.app-manage {
  padding: 0;
}

.search-card {
  margin-bottom: 16px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}

.table-card {
  margin-bottom: 16px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header .title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 16px;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
  margin-top: 4px;
}

.guide-section {
  margin-top: 8px;
}

.code-block {
  background: #f5f7fa;
  border: 1px solid #e4e7ed;
  border-radius: 6px;
  padding: 16px;
  font-size: 13px;
  line-height: 1.6;
  overflow-x: auto;
  color: #303133;
  font-family: 'Courier New', Courier, monospace;
  white-space: pre-wrap;
  word-break: break-all;
}
</style>
