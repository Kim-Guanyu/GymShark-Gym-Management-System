<template>
  <div class="coach-time-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="教练姓名">
          <el-input v-model="searchForm.coachName" placeholder="请输入教练姓名" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="会员姓名">
          <el-input v-model="searchForm.memberName" placeholder="请输入会员姓名" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="预约状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" style="width: 180px;">
            <el-option label="待上课" value="0" />
            <el-option label="已完成" value="1" />
            <el-option label="已取消" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增私教时间</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 数据表格 -->
    <el-card>
      <el-table v-loading="loading" :data="coachTimeList" border stripe class="coach-time-table" style="width:100%;">
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="coachName" label="教练" min-width="100" />
        <el-table-column prop="memberName" label="会员" min-width="100" />
        <el-table-column prop="planTime" label="预约时间" width="180" />
        <el-table-column prop="duration" label="时长(分钟)" width="100" />
        <el-table-column prop="content" label="课程内容" min-width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === '0' ? 'warning' :
              scope.row.status === '1' ? 'success' : 'danger'">
              {{ scope.row.status === '0' ? '待上课' :
                scope.row.status === '1' ? '已完成' : '已取消' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="180" :formatter="formatDate" />
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[10,20,50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="margin-top:20px; text-align:right;"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="coachTimeFormRef" :model="coachTimeForm" :rules="rules" label-width="120px">
        <el-form-item label="教练" prop="coachId">
          <el-select v-model="coachTimeForm.coachId" placeholder="请选择教练" style="width:100%;" @change="handleCoachChange">
            <el-option v-for="item in coachList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="会员姓名" prop="memberName">
          <el-input v-model="coachTimeForm.memberName" placeholder="请输入会员姓名" />
        </el-form-item>
        <el-form-item label="预约时间" prop="planTime">
          <el-date-picker v-model="coachTimeForm.planTime" type="datetime" placeholder="请选择预约时间" style="width:100%;" />
        </el-form-item>
        <el-form-item label="时长(分钟)" prop="duration">
          <el-input-number v-model="coachTimeForm.duration" min="30" step="30" style="width:100%;" />
        </el-form-item>
        <el-form-item label="课程内容" prop="content">
          <el-input v-model="coachTimeForm.content" type="textarea" rows="3" placeholder="如：增肌、减脂、拉伸" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="coachTimeForm.status" placeholder="请选择状态">
            <el-option label="待上课" value="0" />
            <el-option label="已完成" value="1" />
            <el-option label="已取消" value="2" />
          </el-select>
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download } from '@element-plus/icons-vue'
import { getCoachTimePage, addCoachTime, updateCoachTime, deleteCoachTime, exportCoachTimeExcel } from '@/api/coachTime'
import { findAllCoach } from '@/api/coach'
import dayjs from 'dayjs'

// 状态定义
const loading = ref(false)
const coachTimeList = ref([])
const coachList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  coachName: '',
  memberName: '',
  status: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增私教时间')
const coachTimeFormRef = ref(null)
const coachTimeForm = ref({
  id: '',
  coachId: '',
  coachName: '',
  memberName: '',
  planTime: '',
  duration: 60,
  content: '',
  status: '0'
})

// 校验规则
const rules = ref({
  coachId: [{ required: true, message: '请选择教练', trigger: 'change' }],
  memberName: [{ required: true, message: '请输入会员姓名', trigger: 'blur' }],
  planTime: [{ required: true, message: '请选择预约时间', trigger: 'change' }],
  duration: [{ required: true, message: '请输入时长', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
})

// 生命周期
onMounted(() => {
  getList()
  loadCoachList()
})

// 获取列表（核心：映射后端字段到前端展示）
const getList = async () => {
  try {
    loading.value = true
    const res = await getCoachTimePage({
      coachTime: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })

    // 后端数据映射到前端展示字段
    const rawList = res.data?.records || res.data || []
    coachTimeList.value = rawList.map(item => ({
      ...item,
      // 后端startTime → 前端planTime
      planTime: item.startTime || item.planTime || '',
      // 根据coachId匹配教练姓名
      coachName: coachList.value.find(c => c.id === item.coachId)?.name || item.coachName || '未知教练',
      // 补充前端需要的默认值
      memberName: item.memberName || '未知会员',
      duration: item.duration || 60,
      content: item.content || '',
      status: item.status || '0'
    }))

    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('获取私教时间列表失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 加载教练下拉框数据
const loadCoachList = async () => {
  try {
    const res = await findAllCoach()
    coachList.value = res.data || []
  } catch (e) {
    ElMessage.error('加载教练列表失败')
    console.error(e)
  }
}

// 选择教练后自动填充教练姓名
const handleCoachChange = () => {
  const coach = coachList.value.find(item => item.id === coachTimeForm.value.coachId)
  coachTimeForm.value.coachName = coach ? coach.name : ''
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = { coachName: '', memberName: '', status: '' }
  pageNum.value = 1
  getList()
}

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val
  getList()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  getList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增私教时间'
  coachTimeForm.value = {
    id: '',
    coachId: '',
    coachName: '',
    memberName: '',
    planTime: '',
    duration: 60,
    content: '',
    status: '0'
  }
  dialogVisible.value = true
}

// 编辑（回显教练姓名）
const handleEdit = (row) => {
  dialogTitle.value = '编辑私教时间'
  const coach = coachList.value.find(item => item.id === row.coachId)
  coachTimeForm.value = {
    ...row,
    coachName: coach ? coach.name : row.coachName || ''
  }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该私教时间记录？', '提示', { type: 'warning' })
    await deleteCoachTime({ id })
    ElMessage.success('删除成功')
    getList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 提交
const handleSubmit = async () => {
  try {
    await coachTimeFormRef.value.validate()

    // 补充教练姓名（选择教练后自动填充）
    if (coachTimeForm.value.coachId && !coachTimeForm.value.coachName) {
      const coach = coachList.value.find(item => item.id === coachTimeForm.value.coachId)
      coachTimeForm.value.coachName = coach ? coach.name : ''
    }

    if (coachTimeForm.value.id) {
      await updateCoachTime(coachTimeForm.value)
      ElMessage.success('修改成功')
    } else {
      await addCoachTime(coachTimeForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出Excel
const handleExport = async () => {
  try {
    const res = await exportCoachTimeExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `私教时间表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
    a.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 日期格式化
const formatDate = (row, column) => {
  if (!row[column.prop]) return '-'
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm')
}
</script>

<style scoped>
.coach-time-page {
  height: 100%;
  padding: 16px;
}

/* 表格样式：白底黑字，和你现有页面保持一致 */
:deep(.coach-time-table) {
  background-color: #ffffff !important;
}
:deep(.coach-time-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.coach-time-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.coach-time-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.coach-time-table .el-table__row:hover .el-table__cell) {
  background-color: #f0f0f0 !important;
}

/* 分页/卡片样式 */
:deep(.el-pagination) {
  color: #000000 !important;
}
:deep(.el-card) {
  background-color: #ffffff !important;
  border-color: #e5e5e5 !important;
}
:deep(.el-card__header) {
  background-color: #fafafa !important;
  color: #000000 !important;
  border-bottom-color: #e5e5e5 !important;
}
:deep(.el-form-item__label) {
  color: #000000 !important;
}
:deep(.el-input__wrapper), :deep(.el-input-number), :deep(.el-select) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #dcdcdc !important;
}
:deep(.el-input__inner) {
  color: #000000 !important;
}
</style>