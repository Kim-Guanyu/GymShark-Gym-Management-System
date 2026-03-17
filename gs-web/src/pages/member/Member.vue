<template>
  <div class="member-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="会员姓名">
          <el-input v-model="searchForm.name" placeholder="请输入会员姓名" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="会员编号">
          <el-input v-model="searchForm.memberNo" placeholder="请输入会员编号" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="会员卡状态">
          <el-select v-model="searchForm.cardStatus" placeholder="请选择状态" style="width: 180px;">
            <el-option label="正常" value="NORMAL" />
            <el-option label="过期" value="EXPIRED" />
            <el-option label="冻结" value="FROZEN" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getMemberList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增会员</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 会员表格 -->
    <el-card>
      <el-table
          v-loading="loading"
          :data="memberList"
          border
          stripe
          class="member-table"
          style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="memberNo" label="会员编号" width="150" />
        <el-table-column prop="name" label="会员名称" min-width="120" />
        <el-table-column prop="phone" label="联系电话" min-width="120" />
        <el-table-column prop="sex" label="性别" width="80">
          <template #default="scope">
            {{ scope.row.sex === '1' ? '男' : scope.row.sex === '2' ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="cardType" label="卡类型" width="100" />
        <el-table-column prop="stock" label="剩余课程" width="100" />
        <el-table-column prop="cardStatus" label="卡状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.cardStatus === 'NORMAL' ? 'success' :
              scope.row.cardStatus === 'EXPIRED' ? 'danger' : 'warning'">
              {{ scope.row.cardStatus === 'NORMAL' ? '正常' :
                scope.row.cardStatus === 'EXPIRED' ? '过期' : '冻结' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="cardStartTime" label="开卡时间" width="160" :formatter="formatDate" />
        <el-table-column prop="cardEndTime" label="过期时间" width="160" :formatter="formatDate" />
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
      <el-form ref="memberFormRef" :model="memberForm" :rules="memberRules" label-width="120px">
        <el-form-item label="会员编号" prop="memberNo">
          <el-input v-model="memberForm.memberNo" placeholder="系统生成或手动输入" />
        </el-form-item>
        <el-form-item label="会员名称" prop="name">
          <el-input v-model="memberForm.name" placeholder="请输入会员姓名" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="memberForm.password" type="password" placeholder="请输入登录密码" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="memberForm.phone" placeholder="请输入11位手机号码" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="memberForm.sex" placeholder="请选择性别">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="会员卡类型" prop="cardType">
          <el-input v-model="memberForm.cardType" placeholder="如：年卡、季卡、月卡" />
        </el-form-item>
        <el-form-item label="剩余课程" prop="stock">
          <el-input-number v-model="memberForm.stock" min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="会员卡状态" prop="cardStatus">
          <el-select v-model="memberForm.cardStatus" placeholder="请选择状态">
            <el-option label="正常" value="NORMAL" />
            <el-option label="过期" value="EXPIRED" />
            <el-option label="冻结" value="FROZEN" />
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
import { ref,onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download } from '@element-plus/icons-vue'
import {
  getMemberPage, addMember, updateMember, deleteMember, exportMemberExcel
} from '@/api/member'
import dayjs from 'dayjs'

// 状态定义
const loading = ref(false)
const memberList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  name: '',
  memberNo: '',
  cardStatus: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增会员')
const memberFormRef = ref(null)
const memberForm = ref({
  id: '',
  memberNo: '',
  name: '',
  password: '',
  phone: '',
  sex: '',
  cardType: '',
  stock: 0,
  cardStatus: 'NORMAL'
})

// 校验规则
const memberRules = ref({
  memberNo: [{ required: true, message: '请输入会员编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入会员姓名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入登录密码', trigger: 'blur' }],
  phone: [{
    required: true,
    message: '请输入手机号码',
    trigger: 'blur',
    pattern: /^1[3-9]\d{9}$/
  }],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
  cardType: [{ required: true, message: '请输入会员卡类型', trigger: 'blur' }],
  cardStatus: [{ required: true, message: '请选择会员卡状态', trigger: 'change' }]
})

// 生命周期
onMounted(() => {
  getMemberList()
})

// 获取会员列表
const getMemberList = async () => {
  try {
    loading.value = true
    const res = await getMemberPage({
      member: {},
      page: pageNum.value,
      size: pageSize.value
    })
    memberList.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('获取会员列表失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = { name: '', memberNo: '', cardStatus: '' }
  pageNum.value = 1
  getMemberList()
}

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val
  getMemberList()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  getMemberList()
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增会员'
  memberForm.value = {
    id: '',
    memberNo: '',
    name: '',
    password: '',
    phone: '',
    sex: '',
    cardType: '',
    stock: 0,
    cardStatus: 'NORMAL'
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑会员'
  memberForm.value = { ...row }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该会员？', '提示', { type: 'warning' })
    await deleteMember(id)
    ElMessage.success('删除成功')
    getMemberList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await memberFormRef.value.validate()
    if (memberForm.value.id) {
      await updateMember(memberForm.value)
      ElMessage.success('修改成功')
    } else {
      await addMember(memberForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getMemberList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出 Excel
const handleExport = async () => {
  try {
    const res = await exportMemberExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `会员表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
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
.member-page {
  height: 100%;
  padding: 16px;
}

/* 表格样式：白底黑字，与全局风格保持一致 */
:deep(.member-table) {
  background-color: #ffffff !important;
}
:deep(.member-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.member-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.member-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.member-table .el-table__row:hover .el-table__cell) {
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
:deep(.el-input__wrapper), :deep(.el-input-number), :deep(.el-select) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #dcdcdc !important;
}
</style>