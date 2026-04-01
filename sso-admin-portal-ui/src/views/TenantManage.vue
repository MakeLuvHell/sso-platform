<template>
  <div class="page-container">
    <!-- 搜索栏 -->
    <el-card shadow="hover">
      <div class="search-bar">
        <el-input
          v-model="searchForm.name"
          placeholder="请输入租户名称"
          clearable
          style="width: 200px;"
          :prefix-icon="OfficeBuilding"
        />
        <el-select v-model="searchForm.status" placeholder="状态筛选" clearable style="width: 140px;">
          <el-option label="启用" :value="1" />
          <el-option label="禁用" :value="0" />
        </el-select>
        <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
        <el-button :icon="Refresh" @click="handleReset">重置</el-button>
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增租户</el-button>
      </div>
    </el-card>

    <!-- 表格 -->
    <el-card shadow="hover" style="margin-top: 16px;">
      <el-table :data="tableData" stripe border style="width: 100%;">
        <el-table-column prop="name" label="租户名称" width="150" />
        <el-table-column prop="code" label="租户编码" width="140" />
        <el-table-column prop="contact" label="联系人" width="100" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="expireTime" label="到期时间" width="170" />
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
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="90px">
        <el-form-item label="租户名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入租户名称" />
        </el-form-item>
        <el-form-item label="租户编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入租户编码" :disabled="isEdit" />
        </el-form-item>
        <el-form-item label="联系人" prop="contact">
          <el-input v-model="form.contact" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="到期时间" prop="expireTime">
          <el-date-picker
            v-model="form.expireTime"
            type="datetime"
            placeholder="请选择到期时间"
            style="width: 100%;"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
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
import { Search, Refresh, Plus, Edit, Delete, OfficeBuilding } from '@element-plus/icons-vue'

const searchForm = reactive({
  name: '',
  status: null
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

const form = reactive({
  id: null,
  name: '',
  code: '',
  contact: '',
  phone: '',
  expireTime: '',
  status: 1
})

const formRules = {
  name: [{ required: true, message: '请输入租户名称', trigger: 'blur' }],
  code: [{ required: true, message: '请输入租户编码', trigger: 'blur' }],
  contact: [{ required: true, message: '请输入联系人', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入联系电话', trigger: 'blur' }],
  expireTime: [{ required: true, message: '请选择到期时间', trigger: 'change' }]
}

const allData = ref([
  { id: 1, name: '默认租户', code: 'DEFAULT', contact: '管理员', phone: '13800138000', expireTime: '2027-12-31 23:59:59', status: 1 },
  { id: 2, name: '测试公司', code: 'TEST_CORP', contact: '张三', phone: '13800138001', expireTime: '2026-12-31 23:59:59', status: 1 },
  { id: 3, name: '示例企业', code: 'DEMO_ENT', contact: '李四', phone: '13800138002', expireTime: '2026-06-30 23:59:59', status: 1 },
  { id: 4, name: '科技发展公司', code: 'TECH_DEV', contact: '王五', phone: '13800138003', expireTime: '2026-03-31 23:59:59', status: 0 },
  { id: 5, name: '创新科技', code: 'INNO_TECH', contact: '赵六', phone: '13800138004', expireTime: '2027-06-30 23:59:59', status: 1 },
  { id: 6, name: '数据服务公司', code: 'DATA_SVC', contact: '孙七', phone: '13800138005', expireTime: '2027-01-31 23:59:59', status: 1 },
  { id: 7, name: '云平台公司', code: 'CLOUD_PLT', contact: '周八', phone: '13800138006', expireTime: '2026-09-30 23:59:59', status: 1 },
  { id: 8, name: '安全科技公司', code: 'SEC_TECH', contact: '吴九', phone: '13800138007', expireTime: '2026-04-30 23:59:59', status: 0 }
])

const tableData = ref([...allData.value])

const getFilteredData = () => {
  let data = [...allData.value]
  if (searchForm.name) {
    data = data.filter(item => item.name.includes(searchForm.name))
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
  searchForm.name = ''
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
  dialogTitle.value = '新增租户'
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑租户'
  Object.assign(form, { ...row })
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`确定要删除租户 "${row.name}" 吗？`, '提示', {
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
  form.contact = ''
  form.phone = ''
  form.expireTime = ''
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
