<template>
  <div class="course-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="课程名称">
          <el-input v-model="searchForm.name" placeholder="请输入课程名称" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="教练ID">
          <el-input v-model="searchForm.coachId" placeholder="请输入教练ID" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="课程状态">
          <el-select v-model="searchForm.courseStatus" placeholder="请选择课程状态" style="width: 180px;">
            <el-option label="未开始" value="NOT_START" />
            <el-option label="进行中" value="ON_GOING" />
            <el-option label="已结束" value="FINISHED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getCourseList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增公共课程</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 公共课程表格 -->
    <el-card>
      <el-table
          v-loading="loading"
          :data="courseList"
          border
          stripe
          class="course-table"
          style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="name" label="课程名称" min-width="150" />
        <el-table-column prop="coachId" label="教练ID" width="100">
          <template #default="scope">
            {{ scope.row.coachId || '-' }} / {{ getCoachName(scope.row.coachId) }}
          </template>
        </el-table-column>
        <el-table-column prop="maxPeople" label="最大人数" width="100" />
        <el-table-column prop="stock" label="剩余人数" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.stock <= 0 ? 'danger' : 'success'">
              {{ scope.row.stock || 0 }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="courseStartTime" label="开始时间" width="180" :formatter="formatDate" />
        <el-table-column prop="courseEndTime" label="结束时间" width="180" :formatter="formatDate" />
        <el-table-column prop="courseStatus" label="课程状态" width="120">
          <template #default="scope">
            <el-tag
                :type="scope.row.courseStatus === 'NOT_START' ? 'warning' :
                scope.row.courseStatus === 'ON_GOING' ? 'success' : 'info'"
            >
              {{ scope.row.courseStatus === 'NOT_START' ? '未开始' :
                scope.row.courseStatus === 'ON_GOING' ? '进行中' : '已结束' }}
            </el-tag>
          </template>
        </el-table-column>
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
      <el-form ref="courseFormRef" :model="courseForm" :rules="courseRules" label-width="120px">
        <el-form-item label="课程名称" prop="name">
          <el-input v-model="courseForm.name" placeholder="请输入课程名称（如：瑜伽、动感单车）" />
        </el-form-item>
        <el-form-item label="授课教练" prop="coachId">
          <el-select v-model="courseForm.coachId" placeholder="请选择授课教练">
            <el-option v-for="item in coachList" :key="item.id" :label="`${item.id}-${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="最大人数" prop="maxPeople">
          <el-input-number v-model="courseForm.maxPeople" min="1" max="100" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="剩余人数" prop="stock">
          <el-input-number v-model="courseForm.stock" min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="课程开始时间" prop="courseStartTime">
          <el-date-picker
              v-model="courseForm.courseStartTime"
              type="datetime"
              placeholder="请选择开始时间"
              style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="课程结束时间" prop="courseEndTime">
          <el-date-picker
              v-model="courseForm.courseEndTime"
              type="datetime"
              placeholder="请选择结束时间"
              style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="课程状态" prop="courseStatus">
          <el-select v-model="courseForm.courseStatus" placeholder="请选择课程状态">
            <el-option label="未开始" value="NOT_START" />
            <el-option label="进行中" value="ON_GOING" />
            <el-option label="已结束" value="FINISHED" />
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
import { ref, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download } from '@element-plus/icons-vue'
import {
  getCoursePage, addCourse, updateCourse, deleteCourse,
  exportCourseExcel
} from '@/api/course'
import { findAllCoach } from '@/api/coach'
import dayjs from 'dayjs'

// 状态定义
const loading = ref(false)
const courseList = ref([])
const coachList = ref([]) // 教练下拉列表
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  name: '',
  coachId: '',
  courseStatus: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增公共课程')
const courseFormRef = ref(null)
const courseForm = ref({
  id: '',
  name: '',
  coachId: '',
  maxPeople: 10,
  stock: 10,
  courseStartTime: '',
  courseEndTime: '',
  courseStatus: 'NOT_START'
})

// 校验规则
const courseRules = ref({
  name: [{ required: true, message: '请输入课程名称', trigger: 'blur' }],
  coachId: [{ required: true, message: '请选择授课教练', trigger: 'change' }],
  maxPeople: [{ required: true, message: '请输入最大人数', trigger: 'blur' }],
  stock: [{
    required: true,
    message: '请输入剩余人数',
    trigger: 'blur',
    validator: (rule, value, callback) => {
      if (value > courseForm.value.maxPeople) {
        callback(new Error('剩余人数不能超过最大人数'))
      } else {
        callback()
      }
    }
  }],
  courseStartTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  courseEndTime: [{
    required: true,
    message: '请选择结束时间',
    trigger: 'change',
    validator: (rule, value, callback) => {
      if (dayjs(value).isBefore(dayjs(courseForm.value.courseStartTime))) {
        callback(new Error('结束时间不能早于开始时间'))
      } else {
        callback()
      }
    }
  }],
  courseStatus: [{ required: true, message: '请选择课程状态', trigger: 'change' }]
})

// 教练ID转名称（便于显示）
const coachMap = computed(() => {
  return coachList.value.reduce((map, item) => {
    map[item.id] = item.name
    return map
  }, {})
})

// 根据教练ID获取名称
const getCoachName = (coachId) => {
  return coachMap.value[coachId] || '未知教练'
}

// 生命周期
onMounted(() => {
  getCourseList()
  loadCoachList()
})

// 获取公共课程列表
const getCourseList = async () => {
  try {
    loading.value = true
    const res = await getCoursePage({
      course: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })
    courseList.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('获取公共课程失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 加载教练列表
const loadCoachList = async () => {
  const res = await findAllCoach()
  coachList.value = res.data || []
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = { name: '', coachId: '', courseStatus: '' }
  pageNum.value = 1
  getCourseList()
}

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val
  getCourseList()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  getCourseList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增公共课程'
  courseForm.value = {
    id: '',
    name: '',
    coachId: '',
    maxPeople: 10,
    stock: 10,
    courseStartTime: '',
    courseEndTime: '',
    courseStatus: 'NOT_START'
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑公共课程'
  courseForm.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该公共课程？', '提示', { type: 'warning' })
    await deleteCourse({ id })
    ElMessage.success('删除成功')
    getCourseList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await courseFormRef.value.validate()
    if (courseForm.value.id) {
      await updateCourse(courseForm.value)
      ElMessage.success('修改成功')
    } else {
      await addCourse(courseForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getCourseList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出 Excel
const handleExport = async () => {
  try {
    const res = await exportCourseExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `公共课程表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
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
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm:ss')
}
</script>

<style scoped>
.course-page {
  height: 100%;
  padding: 16px;
}

/* 表格样式：白底黑字，与全局风格统一 */
:deep(.course-table) {
  background-color: #ffffff !important;
}
:deep(.course-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.course-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.course-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.course-table .el-table__row:hover .el-table__cell) {
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