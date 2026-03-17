<template>
  <div class="course-enroll-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程ID">
          <el-input v-model="searchForm.courseId" placeholder="请输入课程ID" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="会员ID">
          <el-input v-model="searchForm.memberId" placeholder="请输入会员ID" style="width: 180px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getCourseEnrollList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增报名记录</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 课程报名表格 -->
    <el-card>
      <el-table
          v-loading="loading"
          :data="courseEnrollList"
          border
          stripe
          class="course-enroll-table"
          style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="courseId" label="课程ID" width="100">
          <template #default="scope">
            {{ scope.row.courseId || '-' }} / {{ getCourseName(scope.row.courseId) }}
          </template>
        </el-table-column>
        <el-table-column prop="memberId" label="会员ID" width="100">
          <template #default="scope">
            {{ scope.row.memberId || '-' }} / {{ getMemberName(scope.row.memberId) }}
          </template>
        </el-table-column>
        <el-table-column prop="enrollTime" label="报名时间" width="180" :formatter="formatDate" />
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
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          style="margin-top: 20px; text-align: right;"
      />
    </el-card>

    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="650px" destroy-on-close>
      <el-form ref="courseEnrollFormRef" :model="courseEnrollForm" :rules="courseEnrollRules" label-width="120px">
        <el-form-item label="选择课程" prop="courseId">
          <el-select v-model="courseEnrollForm.courseId" placeholder="请选择报名课程">
            <el-option v-for="item in courseList" :key="item.id" :label="`${item.id}-${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择会员" prop="memberId">
          <el-select v-model="courseEnrollForm.memberId" placeholder="请选择报名会员">
            <el-option v-for="item in memberList" :key="item.id" :label="`${item.id}-${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="报名时间" prop="enrollTime">
          <el-date-picker
              v-model="courseEnrollForm.enrollTime"
              type="datetime"
              placeholder="请选择报名时间"
              style="width: 100%;"
              default-time="12:00:00"
          />
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
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getCourseEnrollPage, addCourseEnroll, updateCourseEnroll, deleteCourseEnroll,
  exportCourseEnrollExcel
} from '@/api/courseEnroll'
import { findAllCourse } from '@/api/course'
import { findAllMember } from '@/api/member'
import dayjs from 'dayjs'

// 状态定义
const loading = ref(false)
const courseEnrollList = ref([])
const courseList = ref([]) // 课程下拉列表
const memberList = ref([]) // 会员下拉列表
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  courseId: '',
  memberId: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增报名记录')
const courseEnrollFormRef = ref(null)
const courseEnrollForm = ref({
  id: '',
  courseId: '',
  memberId: '',
  enrollTime: dayjs().format('YYYY-MM-DD HH:mm:ss'), // 默认当前时间
  deleted: 0
})

// 校验规则
const courseEnrollRules = ref({
  courseId: [{ required: true, message: '请选择报名课程', trigger: 'change' }],
  memberId: [{ required: true, message: '请选择报名会员', trigger: 'change' }],
  enrollTime: [{ required: true, message: '请选择报名时间', trigger: 'change' }]
})

// 课程ID转名称
const courseMap = computed(() => {
  return courseList.value.reduce((map, item) => {
    map[item.id] = item.name
    return map
  }, {})
})

// 会员ID转名称
const memberMap = computed(() => {
  return memberList.value.reduce((map, item) => {
    map[item.id] = item.name
    return map
  }, {})
})

// 根据课程ID获取名称
const getCourseName = (courseId) => {
  return courseMap.value[courseId] || '未知课程'
}

// 根据会员ID获取名称
const getMemberName = (memberId) => {
  return memberMap.value[memberId] || '未知会员'
}

// 生命周期
onMounted(() => {
  getCourseEnrollList()
  loadCourseList()
  loadMemberList()
})

// 获取课程报名列表
const getCourseEnrollList = async () => {
  try {
    loading.value = true
    const res = await getCourseEnrollPage({
      courseEnroll: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })
    courseEnrollList.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('获取报名记录失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 加载课程列表
const loadCourseList = async () => {
  const res = await findAllCourse()
  courseList.value = res.data || []
}

// 加载会员列表
const loadMemberList = async () => {
  const res = await findAllMember()
  memberList.value = res.data || []
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = { courseId: '', memberId: '' }
  pageNum.value = 1
  getCourseEnrollList()
}

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val
  getCourseEnrollList()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  getCourseEnrollList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增报名记录'
  courseEnrollForm.value = {
    id: '',
    courseId: '',
    memberId: '',
    enrollTime: dayjs().format('YYYY-MM-DD HH:mm:ss'),
    deleted: 0
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑报名记录'
  courseEnrollForm.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该报名记录？', '提示', { type: 'warning' })
    await deleteCourseEnroll(id)
    ElMessage.success('删除成功')
    getCourseEnrollList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await courseEnrollFormRef.value.validate()

    if (courseEnrollForm.value.id) {
      await updateCourseEnroll(courseEnrollForm.value)
      ElMessage.success('修改成功')
    } else {
      await addCourseEnroll(courseEnrollForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getCourseEnrollList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出 Excel
const handleExport = async () => {
  try {
    const res = await exportCourseEnrollExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `公共课程报名表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.msg || error.message))
  }
}

// 日期格式化
const formatDate = (row, column) => {
  if (!row[column.prop]) return '-'
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<style scoped>
.course-enroll-page {
  height: 100%;
  padding: 16px;
}

/* 表格样式：白底黑字，与全局风格统一 */
:deep(.course-enroll-table) {
  background-color: #ffffff !important;
}
:deep(.course-enroll-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.course-enroll-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.course-enroll-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.course-enroll-table .el-table__row:hover .el-table__cell) {
  background-color: #f0f0f0 !important;
}

/* 全局样式适配 */
:deep(.el-pagination) {
  color: #000000 !important;
}
:deep(.el-card) {
  background-color: #ffffff !important;
  border-color: #e5e5e5 !important;
}
:deep(.el-form-item__label) {
  color: #000000 !important;
}
:deep(.el-input__wrapper), :deep(.el-input-number), :deep(.el-select), :deep(.el-date-picker) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #dcdcdc !important;
}
:deep(.el-input__inner) {
  color: #000000 !important;
}
</style>