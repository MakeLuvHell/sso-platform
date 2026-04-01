<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="hover">
      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入角色名称"
          clearable
          style="width: 200px;"
          :prefix-icon="UserFilled"
        />
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增角色</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="hover" style="margin-top: 16px;">
      <el-table :data="tableData" stripe border style="width: 100%;">
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="code" label="角色编码" width="160" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" size="small">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" :icon="Edit" @click="handleEdit(row)">编辑</el-button>
            <el-button type="warning" link size="small" :icon="Key" @click="handleAssignPermission(row)">分配权限</el-button>
            <el-button type="danger" link size="small" :icon="Delete" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :page-sizes="[10, 20, 50]"
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
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入角色描述" />
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

    <!-- 分配权限对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="480px"
    >
      <div style="max-height: 400px; overflow-y: auto;">
        <el-tree
          ref="permissionTreeRef"
          :data="permissionTree"
          show-checkbox
          node-key="id"
          :default-checked-keys="checkedPermissions"
          :props="{ label: 'label', children: 'children' }"
        />
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermission">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus, Edit, Delete, Key, UserFilled } from '@element-plus/icons-vue'

const searchForm = reactive({
  name: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 8
})

const dialogVisible = ref(false)
const dialogTitle = ref('')
const isEdit = ref(false)
const formRef = ref(null)

const permissionDialogVisible = ref(false)
const permissionTreeRef = ref(null)
const currentRoleId = ref(null)
const checkedPermissions = ref([])

const form = reactive({
  id: null,
  name: '',
  code: '',
  description: '',
  status: 1
})

const formRules = {
  name: [{ required: true, message: '请输入角色名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入角色编码', trigger: 'blur' }]
}

const allData = ref([
  { id: 1, name: '超级管理员', code: 'SUPER_ADMIN', description: '拥有系统所有权限', status: 1 },
  { id: 2, name: '系统管理员', code: 'SYS_ADMIN', description: '管理系统配置和用户', status: 1 },
  { id: 3, name: '租户管理员', code: 'TENANT_ADMIN', description: '管理租户相关信息', status: 1 },
  { id: 4, name: '普通用户', code: 'USER', description: '普通用户角色', status: 1 },
  { id: 5, name: '审计员', code: 'AUDITOR', description: '查看审计日志', status: 1 },
  { id: 6, name: '只读用户', code: 'READONLY', description: '只读权限', status: 1 },
  { id: 7, name: '运维人员', code: 'OPS', description: '系统运维相关权限', status: 0 },
  { id: 8, name: '访客', code: 'GUEST', description: '访客权限', status: 0 }
])

const tableData = ref([...allData.value])

// 权限树形数据
const permissionTree = reactive([
  {
    id: 1,
    label: '仪表盘',
    children: [
      { id: 11, label: '查看仪表盘' }
    ]
  },
  {
    id: 2,
    label: '用户管理',
    children: [
      { id: 21, label: '查看用户' },
      { id: 22, label: '新增用户' },
      { id: 23, label: '编辑用户' },
      { id: 24, label: '删除用户' }
    ]
  },
  {
    id: 3,
    label: '租户管理',
    children: [
      { id: 31, label: '查看租户' },
      { id: 32, label: '新增租户' },
      { id: 33, label: '编辑租户' },
      { id: 34, label: '删除租户' }
    ]
  },
  {
    id: 4,
    label: '角色管理',
    children: [
      { id: 41, label: '查看角色' },
      { id: 42, label: '新增角色' },
      { id: 43, label: '编辑角色' },
      { id: 44, label: '删除角色' },
      { id: 45, label: '分配权限' }
    ]
  },
  {
    id: 5,
    label: '权限管理',
    children: [
      { id: 51, label: '查看权限' },
      { id: 52, label: '新增权限' },
      { id: 53, label: '编辑权限' },
      { id: 54, label: '删除权限' }
    ]
  },
  {
    id: 6,
    label: '审计日志',
    children: [
      { id: 61, label: '查看日志' },
      { id: 62, label: '导出日志' }
    ]
  }
])

const getFilteredData = () => {
  let data = [...allData.value]
  if (searchForm.name) {
    data = data.filter(item => item.name.includes(searchForm.name))
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
  searchForm.name = ''
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
  dialogTitle.value = '新增角色'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑角色'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除角色 "${row.name}" 吗？`, '提示', {
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

const handleAssignPermission = (row) => {
  currentRoleId.value = row.id
  // 模拟已分配权限
  if (row.code === 'SUPER_ADMIN') {
    checkedPermissions.value = [11, 21, 22, 23, 24, 31, 32, 33, 34, 41, 42, 43, 44, 45, 51, 52, 53, 54, 61, 62]
  } else if (row.code === 'SYS_ADMIN') {
    checkedPermissions.value = [11, 21, 22, 23, 24, 41, 42, 43, 44, 45]
  } else {
    checkedPermissions.value = [11]
  }
  permissionDialogVisible.value = true
}

const handleSavePermission = () => {
  ElMessage.success('权限分配成功（模拟）')
  permissionDialogVisible.value = false
}

const resetForm = () => {
  form.id = null
  form.name = ''
  form.code = ''
  form.description = ''
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
