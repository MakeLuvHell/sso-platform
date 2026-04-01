<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="hover">
      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入权限名称"
          clearable
          style="width: 200px;"
          :prefix-icon="Lock"
        />
        <el-select v-model="searchForm.type" placeholder="权限类型" clearable style="width: 140px;">
          <el-option label="菜单" value="菜单" />
          <el-option label="按钮" value="按钮" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增权限</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="hover" style="margin-top: 16px;">
      <el-table :data="tableData" stripe border style="width: 100%;" row-key="id" default-expand-all>
        <el-table-column prop="name" label="权限名称" width="180" />
        <el-table-column prop="code" label="权限编码" width="200" />
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="row.type === '菜单' ? 'primary' : 'success'" size="small">
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="path" label="路径" min-width="180" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="520px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="80px">
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入权限编码" />
        </el-form-item>
        <el-form-item label="权限类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio value="菜单">菜单</el-radio>
            <el-radio value="按钮">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路径" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
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
import { Search, Refresh, Plus, Edit, Delete, Lock } from '@element-plus/icons-vue'

const searchForm = reactive({
  name: '',
  type: ''
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const form = reactive({
  id: null,
  name: '',
  code: '',
  type: '菜单',
  path: '',
  sort: 0,
  status: 1
})

const formRules = {
  name: [{ required: true, message: '请输入权限名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入权限编码', trigger: 'blur' }],
  type: [{ required: true, message: '请选择权限类型', trigger: 'change' }]
}

const allData = ref([
  { id: 1, name: '仪表盘', code: 'dashboard', type: '菜单', path: '/dashboard', sort: 1, status: 1 },
  { id: 2, name: '用户管理', code: 'user:manage', type: '菜单', path: '/user', sort: 2, status: 1 },
  { id: 3, name: '查看用户', code: 'user:view', type: '按钮', path: '', sort: 1, status: 1 },
  { id: 4, name: '新增用户', code: 'user:add', type: '按钮', path: '', sort: 2, status: 1 },
  { id: 5, name: '编辑用户', code: 'user:edit', type: '按钮', path: '', sort: 3, status: 1 },
  { id: 6, name: '删除用户', code: 'user:delete', type: '按钮', path: '', sort: 4, status: 1 },
  { id: 7, name: '租户管理', code: 'tenant:manage', type: '菜单', path: '/tenant', sort: 3, status: 1 },
  { id: 8, name: '查看租户', code: 'tenant:view', type: '按钮', path: '', sort: 1, status: 1 },
  { id: 9, name: '新增租户', code: 'tenant:add', type: '按钮', path: '', sort: 2, status: 1 },
  { id: 10, name: '编辑租户', code: 'tenant:edit', type: '按钮', path: '', sort: 3, status: 1 },
  { id: 11, name: '删除租户', code: 'tenant:delete', type: '按钮', path: '', sort: 4, status: 1 },
  { id: 12, name: '角色管理', code: 'role:manage', type: '菜单', path: '/role', sort: 4, status: 1 },
  { id: 13, name: '查看角色', code: 'role:view', type: '按钮', path: '', sort: 1, status: 1 },
  { id: 14, name: '新增角色', code: 'role:add', type: '按钮', path: '', sort: 2, status: 1 },
  { id: 15, name: '编辑角色', code: 'role:edit', type: '按钮', path: '', sort: 3, status: 1 },
  { id: 16, name: '删除角色', code: 'role:delete', type: '按钮', path: '', sort: 4, status: 1 },
  { id: 17, name: '分配权限', code: 'role:assign', type: '按钮', path: '', sort: 5, status: 1 },
  { id: 18, name: '权限管理', code: 'permission:manage', type: '菜单', path: '/permission', sort: 5, status: 1 },
  { id: 19, name: '审计日志', code: 'audit:log', type: '菜单', path: '/audit', sort: 6, status: 1 },
  { id: 20, name: '查看日志', code: 'audit:view', type: '按钮', path: '', sort: 1, status: 1 },
  { id: 21, name: '导出日志', code: 'audit:export', type: '按钮', path: '', sort: 2, status: 1 }
])

const tableData = ref([...allData.value])

const getFilteredData = () => {
  let data = [...allData.value]
  if (searchForm.name) {
    data = data.filter(item => item.name.includes(searchForm.name))
  }
  if (searchForm.type) {
    data = data.filter(item => item.type === searchForm.type)
  }
  return data
}

const handleSearch = () => {
  tableData.value = getFilteredData()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.type = ''
  tableData.value = getFilteredData()
}

const handleAdd = () => {
  isEdit.value = false
  dialogTitle.value = '新增权限'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑权限'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除权限 "${row.name}" 吗？`, '提示', {
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

const resetForm = () => {
  form.id = null
  form.name = ''
  form.code = ''
  form.type = '菜单'
  form.path = ''
  form.sort = 0
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
      allData.value.unshift({ ...form, id: newId })
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    tableData.value = getFilteredData()
  })
}
</script>
