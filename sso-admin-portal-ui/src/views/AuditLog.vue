<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="hover">
      <div class="search-bar">
        <el-input
          v-model="searchForm.module"
          placeholder="操作模块"
          clearable
          style="width: 160px;"
        />
        <el-select v-model="searchForm.type" placeholder="操作类型" clearable style="width: 140px;">
          <el-option label="登录" value="登录" />
          <el-option label="新增" value="新增" />
          <el-option label="编辑" value="编辑" />
          <el-option label="删除" value="删除" />
          <el-option label="查询" value="查询" />
          <el-option label="导出" value="导出" />
        </el-select>
        <el-input
          v-model="searchForm.operator"
          placeholder="操作人"
          clearable
          style="width: 140px;"
        />
        <el-date-picker
          v-model="searchForm.dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="YYYY-MM-DD"
          style="width: 260px;"
        />
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="hover" style="margin-top: 16px;">
      <el-table :data="tableData" stripe border style="width: 100%;">
        <el-table-column prop="module" label="操作模块" width="120" />
        <el-table-column prop="type" label="操作类型" width="100">
          <template #default="{ row }">
            <el-tag
              :type="getTypeTag(row.type)"
              size="small"
            >
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="operator" label="操作人" width="100" />
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="time" label="操作时间" width="170" />
        <el-table-column prop="description" label="操作描述" min-width="250" />
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Search, Refresh } from '@element-plus/icons-vue'

const searchForm = reactive({
  module: '',
  type: '',
  operator: '',
  dateRange: null
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 30
})

const allData = ref([
  { module: '用户管理', type: '登录', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:15:32', description: '管理员登录系统' },
  { module: '用户管理', type: '查询', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:16:05', description: '查询用户列表' },
  { module: '用户管理', type: '新增', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:20:18', description: '新增用户 chenyi' },
  { module: '用户管理', type: '编辑', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:25:33', description: '编辑用户 zhangsan 的信息' },
  { module: '用户管理', type: '删除', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:30:45', description: '删除用户 testuser' },
  { module: '租户管理', type: '查询', operator: 'zhangsan', ip: '192.168.1.101', time: '2026-04-01 09:18:22', description: '查询租户列表' },
  { module: '租户管理', type: '新增', operator: 'zhangsan', ip: '192.168.1.101', time: '2026-04-01 09:22:10', description: '新增租户 新科技公司' },
  { module: '角色管理', type: '查询', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:35:20', description: '查询角色列表' },
  { module: '角色管理', type: '编辑', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:40:15', description: '编辑角色 系统管理员 的权限' },
  { module: '权限管理', type: '查询', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:45:30', description: '查询权限列表' },
  { module: '审计日志', type: '查询', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:50:00', description: '查询审计日志' },
  { module: '审计日志', type: '导出', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 09:55:12', description: '导出审计日志（2026-03）' },
  { module: '用户管理', type: '登录', operator: 'lisi', ip: '10.0.0.55', time: '2026-04-01 08:58:45', description: '用户 lisi 登录系统' },
  { module: '用户管理', type: '登录', operator: 'wangwu', ip: '172.16.0.88', time: '2026-04-01 08:45:22', description: '用户 wangwu 登录失败（密码错误）' },
  { module: '用户管理', type: '登录', operator: 'zhaoliu', ip: '192.168.2.200', time: '2026-04-01 08:30:11', description: '用户 zhaoliu 登录系统' },
  { module: '用户管理', type: '编辑', operator: 'zhangsan', ip: '192.168.1.101', time: '2026-04-01 10:05:30', description: '编辑用户 lisi 的邮箱信息' },
  { module: '租户管理', type: '编辑', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 10:10:45', description: '更新租户 测试公司 的到期时间' },
  { module: '用户管理', type: '登录', operator: 'sunqi', ip: '192.168.1.102', time: '2026-04-01 10:15:00', description: '用户 sunqi 登录系统' },
  { module: '角色管理', type: '新增', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 10:20:30', description: '新增角色 数据分析师' },
  { module: '权限管理', type: '编辑', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 10:25:15', description: '更新权限 审计日志 的状态' },
  { module: '用户管理', type: '登录', operator: 'zhouba', ip: '192.168.1.103', time: '2026-04-01 10:30:00', description: '用户 zhouba 登录系统' },
  { module: '用户管理', type: '查询', operator: 'lisi', ip: '10.0.0.55', time: '2026-04-01 10:35:20', description: '查询用户列表' },
  { module: '租户管理', type: '删除', operator: 'admin', ip: '192.168.1.100', time: '2026-04-01 10:40:10', description: '删除租户 临时测试租户' },
  { module: '用户管理', type: '登录', operator: 'admin', ip: '192.168.1.100', time: '2026-03-31 17:30:00', description: '管理员登录系统' },
  { module: '审计日志', type: '查询', operator: 'admin', ip: '192.168.1.100', time: '2026-03-31 17:35:20', description: '查询审计日志' },
  { module: '用户管理', type: '编辑', operator: 'admin', ip: '192.168.1.100', time: '2026-03-31 17:40:15', description: '批量启用用户' },
  { module: '角色管理', type: '查询', operator: 'zhangsan', ip: '192.168.1.101', time: '2026-03-31 14:20:30', description: '查询角色列表' },
  { module: '用户管理', type: '登录', operator: 'zhangsan', ip: '192.168.1.101', time: '2026-03-31 14:15:00', description: '用户 zhangsan 登录系统' },
  { module: '权限管理', type: '新增', operator: 'admin', ip: '192.168.1.100', time: '2026-03-31 11:10:45', description: '新增权限 数据导出' },
  { module: '用户管理', type: '登录', operator: 'admin', ip: '192.168.1.100', time: '2026-03-31 09:00:00', description: '管理员登录系统' }
])

const tableData = ref([...allData.value])

const getTypeTag = (type) => {
  const map = {
    '登录': '',
    '新增': 'success',
    '编辑': 'warning',
    '删除': 'danger',
    '查询': 'info',
    '导出': 'primary'
  }
  return map[type] || 'info'
}

const getFilteredData = () => {
  let data = [...allData.value]
  if (searchForm.module) {
    data = data.filter(item => item.module.includes(searchForm.module))
  }
  if (searchForm.type) {
    data = data.filter(item => item.type === searchForm.type)
  }
  if (searchForm.operator) {
    data = data.filter(item => item.operator.includes(searchForm.operator))
  }
  if (searchForm.dateRange && searchForm.dateRange.length === 2) {
    const [start, end] = searchForm.dateRange
    data = data.filter(item => {
      const date = item.time.split(' ')[0]
      return date >= start && date <= end
    })
  }
  pagination.total = data.length
  const startIndex = (pagination.page - 1) * pagination.pageSize
  return data.slice(startIndex, startIndex + pagination.pageSize)
}

const handleSearch = () => {
  pagination.page = 1
  tableData.value = getFilteredData()
}

const handleReset = () => {
  searchForm.module = ''
  searchForm.type = ''
  searchForm.operator = ''
  searchForm.dateRange = null
  pagination.page = 1
  tableData.value = getFilteredData()
}

const handleSizeChange = () => {
  tableData.value = getFilteredData()
}

const handleCurrentChange = () => {
  tableData.value = getFilteredData()
}
</script>
