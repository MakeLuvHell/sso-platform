<template>
  <div class="page-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-icon :size="48" color="#409EFF"><User /></el-icon>
          <div class="stat-number">{{ stats.totalUsers }}</div>
          <div class="stat-label">用户总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-icon :size="48" color="#67C23A"><OfficeBuilding /></el-icon>
          <div class="stat-number">{{ stats.totalTenants }}</div>
          <div class="stat-label">租户总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-icon :size="48" color="#E6A23C"><UserFilled /></el-icon>
          <div class="stat-number">{{ stats.totalRoles }}</div>
          <div class="stat-label">角色总数</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card" shadow="hover">
          <el-icon :size="48" color="#F56C6C"><Odometer /></el-icon>
          <div class="stat-number">{{ stats.todayLogins }}</div>
          <div class="stat-label">今日登录次数</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 登录趋势图 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>近7天登录趋势</span>
        </div>
      </template>
      <div class="chart-container">
        <div class="chart-bars">
          <div
            v-for="(item, index) in loginTrend"
            :key="index"
            class="chart-bar-wrapper"
          >
            <div class="chart-bar-value">{{ item.count }}</div>
            <div
              class="chart-bar"
              :style="{ height: (item.count / maxCount * 100) + '%' }"
            ></div>
            <div class="chart-bar-label">{{ item.date }}</div>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 最近登录记录 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <template #header>
        <div class="card-header">
          <span>最近登录记录</span>
        </div>
      </template>
      <el-table :data="recentLogins" stripe>
        <el-table-column prop="username" label="用户名" width="120" />
        <el-table-column prop="name" label="姓名" width="120" />
        <el-table-column prop="ip" label="IP地址" width="150" />
        <el-table-column prop="location" label="登录地点" />
        <el-table-column prop="browser" label="浏览器" width="150" />
        <el-table-column prop="loginTime" label="登录时间" width="180" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '成功' ? 'success' : 'danger'" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, computed } from 'vue'
import { User, OfficeBuilding, UserFilled, Odometer } from '@element-plus/icons-vue'

const stats = reactive({
  totalUsers: 1286,
  totalTenants: 52,
  totalRoles: 18,
  todayLogins: 342
})

const loginTrend = reactive([
  { date: '03-26', count: 256 },
  { date: '03-27', count: 312 },
  { date: '03-28', count: 289 },
  { date: '03-29', count: 356 },
  { date: '03-30', count: 298 },
  { date: '03-31', count: 324 },
  { date: '04-01', count: 342 }
])

const maxCount = computed(() => Math.max(...loginTrend.map(i => i.count)))

const recentLogins = reactive([
  { username: 'admin', name: '系统管理员', ip: '192.168.1.100', location: '北京市', browser: 'Chrome 122', loginTime: '2026-04-01 09:15:32', status: '成功' },
  { username: 'zhangsan', name: '张三', ip: '192.168.1.101', location: '上海市', browser: 'Firefox 123', loginTime: '2026-04-01 09:12:18', status: '成功' },
  { username: 'lisi', name: '李四', ip: '10.0.0.55', location: '广州市', browser: 'Edge 121', loginTime: '2026-04-01 08:58:45', status: '成功' },
  { username: 'wangwu', name: '王五', ip: '172.16.0.88', location: '深圳市', browser: 'Chrome 122', loginTime: '2026-04-01 08:45:22', status: '失败' },
  { username: 'zhaoliu', name: '赵六', ip: '192.168.2.200', location: '杭州市', browser: 'Safari 17', loginTime: '2026-04-01 08:30:11', status: '成功' }
])
</script>

<style scoped>
.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-4px);
}

.stat-card .stat-number {
  font-size: 32px;
  font-weight: bold;
  color: #409EFF;
  margin: 10px 0;
}

.stat-card .stat-label {
  font-size: 14px;
  color: #909399;
}

.card-header {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.chart-container {
  height: 260px;
  padding: 20px 0;
}

.chart-bars {
  display: flex;
  align-items: flex-end;
  justify-content: space-around;
  height: 100%;
  padding: 0 20px;
}

.chart-bar-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  height: 100%;
  justify-content: flex-end;
}

.chart-bar-value {
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

.chart-bar {
  width: 40px;
  background: linear-gradient(180deg, #409EFF, #79bbff);
  border-radius: 4px 4px 0 0;
  min-height: 4px;
  transition: height 0.5s ease;
}

.chart-bar-label {
  font-size: 12px;
  color: #909399;
  margin-top: 8px;
}
</style>
