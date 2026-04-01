<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="hover" class="search-card">
      <div class="search-bar">
        <el-input
          v-model="searchForm.username"
          placeholder="请输入用户名"
          clearable
          style="width: 200px;"
          :prefix-icon="User"
        />
        <el-select v-model="searchForm.status" placeholder="状态筛选" clearable style="width: 140px;">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增用户</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="hover" style="margin-top: 16px;">
      <el-table :data="tableData" stripe border style="width: 100%;">
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="email" label="邮箱" min-width="180" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="tenant" label="租户" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-text="启用"
              inactive-text="禁用"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="170" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="520px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="租户" prop="tenant">
          <el-select v-model="form.tenant" placeholder="请选择租户" style="width: 100%;">
            <el-option label="默认租户" value="默认租户" />
            <el-option label="测试公司" value="测试公司" />
            <el-option label="示例企业" value="示例企业" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="!isEdit" label="密码" prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, User } from '@element-plus/icons-vue'

const searchForm = reactive({
  username: '',
  status: null
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 25
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  username: '',
  name: '',
  email: '',
  phone: '',
  tenant: '',
  password: '',
  status: 1
})

const formRules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  tenant: [{ required: true, message: '请选择租户', trigger: 'change' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 模拟数据
const allData = ref([
  { id: 1, username: 'admin', name: '系统管理员', email: 'admin@sso.com', phone: '13800138000', tenant: '默认租户', status: 1, createTime: '2026-01-01 10:00:00' },
  { id: 2, username: 'zhangsan', name: '张三', email: 'zhangsan@example.com', phone: '13800138001', tenant: '测试公司', status: 1, createTime: '2026-01-15 14:30:00' },
  { id: 3, username: 'lisi', name: '李四', email: 'lisi@example.com', phone: '13800138002', tenant: '测试公司', status: 1, createTime: '2026-02-01 09:20:00' },
  { id: 4, username: 'wangwu', name: '王五', email: 'wangwu@example.com', phone: '13800138003', tenant: '示例企业', status: 0, createTime: '2026-02-10 16:45:00' },
  { id: 5, username: 'zhaoliu', name: '赵六', email: 'zhaoliu@example.com', phone: '13800138004', tenant: '示例企业', status: 1, createTime: '2026-02-20 11:15:00' },
  { id: 6, username: 'sunqi', name: '孙七', email: 'sunqi@example.com', phone: '13800138005', tenant: '默认租户', status: 1, createTime: '2026-03-01 08:30:00' },
  { id: 7, username: 'zhouba', name: '周八', email: 'zhouba@example.com', phone: '13800138006', tenant: '测试公司', status: 1, createTime: '2026-03-05 13:00:00' },
  { id: 8, username: 'wujiu', name: '吴九', email: 'wujiu@example.com', phone: '13800138007', tenant: '示例企业', status: 0, createTime: '2026-03-10 10:20:00' },
  { id: 9, username: 'zhengshi', name: '郑十', email: 'zhengshi@example.com', phone: '13800138008', tenant: '默认租户', status: 1, createTime: '2026-03-15 15:40:00' },
  { id: 10, username: 'chenyi', name: '陈一', email: 'chenyi@example.com', phone: '13800138009', tenant: '测试公司', status: 1, createTime: '2026-03-20 09:00:00' }
])

const tableData = ref([...allData.value])

const getFilteredData = () => {
  let data = [...allData.value]
  if (searchForm.username) {
    data = data.filter(item => item.username.includes(searchForm.username))
  }
  if (searchForm.status !== null && searchForm.status !== '') {
    data = data.filter(item => item.status === searchForm.status)
  }
  pagination.total = data.length
  const start = (pagination.page - 1) * pagination.pageSize
  return data.slice(start, start + pagination.pageSize)
}

const handleSearch = () => {
  pagination.page = 1
  tableData.value = getFilteredData()
}

const handleReset = () => {
  searchForm.username = ''
  searchForm.status = null
  pagination.page = 1
  tableData.value = getFilteredData()
}

const handleSizeChange = () => {
  tableData.value = getFilteredData()
}

const handleCurrentChange = () => {
  tableData.value = getFilteredData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增用户'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑用户'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除用户 "${row.name}" 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    const index = allData.value.findIndex(item => item.id === row.id)
    if (index > -1) {
      allData.value.splice(index, 1)
      tableData.value = getFilteredData()
      ElMessage.success('删除成功')
    }
  }).catch(() => {})
}

const handleStatusChange = (row) => {
  ElMessage.success(`用户 "${row.name}" 状态已更新为${row.status === 1 ? '启用' : '禁用'}`)
}

const resetForm = () => {
  form.id = null
  form.username = ''
  form.name = ''
  form.email = ''
  form.phone = ''
  form.tenant = ''
  form.password = ''
  form.status = 1
  formRef.value?.resetFields()
}

const handleSubmit = () => {
  formRef.value.validate((valid) => {
    if (!valid) return
    if (isEdit.value) {
      const index = allData.value.findIndex(item => item.id === form.id)
      if (index > -1) {
        allData.value[index] = { ...form }
      }
      ElMessage.success('编辑成功')
    } else {
      const newId = Math.max(...allData.value.map(i => i.id)) + 1
      allData.value.unshift({
        ...form,
        id: newId,
        createTime: new Date().toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-')
      })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    tableData.value = getFilteredData()
  })
}
</script>

<style scoped>
.search-card {
  margin-bottom: 0;
}
</style>
