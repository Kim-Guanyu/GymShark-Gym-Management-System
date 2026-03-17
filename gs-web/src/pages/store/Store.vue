<template>
  <div class="store-page">
    <!-- 搜索栏 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="会员ID">
          <el-input v-model="searchForm.memberId" placeholder="请输入会员ID" style="width: 180px;" />
        </el-form-item>
        <el-form-item label="资产ID">
          <el-input v-model="searchForm.assetsId" placeholder="请输入资产ID" style="width: 180px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getStoreList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增购买记录</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 购买记录表格 -->
    <el-card>
      <el-table
          v-loading="loading"
          :data="storeList"
          border
          stripe
          class="store-table"
          style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column prop="memberId" label="会员ID" width="100" />
        <el-table-column prop="assetsId" label="资产ID" width="100" />
        <el-table-column prop="quantity" label="购买数量" width="100" />
        <el-table-column prop="unitPrice" label="单价" width="120">
          <template #default="scope">
            ¥{{ scope.row.unitPrice || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="totalPrice" label="总价" width="120">
          <template #default="scope">
            ¥{{ scope.row.totalPrice || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="created" label="购买时间" width="180" :formatter="formatDate" />
        <el-table-column prop="updated" label="更新时间" width="180" :formatter="formatDate" />
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
      <el-form ref="storeFormRef" :model="storeForm" :rules="storeRules" label-width="120px">
        <el-form-item label="会员ID" prop="memberId">
          <el-select v-model="storeForm.memberId" placeholder="请选择会员">
            <el-option v-for="item in memberList" :key="item.id" :label="`${item.id}-${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="资产ID" prop="assetsId">
          <el-select v-model="storeForm.assetsId" placeholder="请选择资产">
            <el-option v-for="item in assetsList" :key="item.id" :label="`${item.id}-${item.name}`" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="购买数量" prop="quantity">
          <el-input-number v-model="storeForm.quantity" min="1" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="商品单价" prop="unitPrice">
          <el-input
              v-model="storeForm.unitPrice"
              placeholder="请输入单价（保留2位小数）"
              @blur="calcTotalPrice"
          />
        </el-form-item>
        <el-form-item label="商品总价" prop="totalPrice">
          <el-input v-model="storeForm.totalPrice" placeholder="自动计算或手动输入" disabled />
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
import {
  getStorePage, addStore, updateStore, deleteStore,
  getAllMember, getAllAssets, exportStoreExcel
} from '@/api/store'
import dayjs from 'dayjs'

// 状态定义
const loading = ref(false)
const storeList = ref([])
const memberList = ref([]) // 会员下拉列表
const assetsList = ref([]) // 资产下拉列表
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  memberId: '',
  assetsId: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增购买记录')
const storeFormRef = ref(null)
const storeForm = ref({
  id: '',
  memberId: '',
  assetsId: '',
  quantity: 1,
  unitPrice: '',
  totalPrice: ''
})

// 校验规则
const storeRules = ref({
  memberId: [{ required: true, message: '请选择会员', trigger: 'change' }],
  assetsId: [{ required: true, message: '请选择资产', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入购买数量', trigger: 'blur' }],
  unitPrice: [{
    required: true,
    message: '请输入商品单价',
    trigger: 'blur',
    pattern: /^\d+(\.\d{1,2})?$/ // 校验保留2位小数
  }]
})

// 生命周期
onMounted(() => {
  getStoreList()
  loadMemberList()
  loadAssetsList()
})

// 获取购买记录列表
const getStoreList = async () => {
  try {
    loading.value = true
    const res = await getStorePage({
      store: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })
    storeList.value = res.data?.records || res.data || []
    total.value = res.data?.total || 0
  } catch (e) {
    ElMessage.error('获取购买记录失败')
    console.error(e)
  } finally {
    loading.value = false
  }
}

// 加载会员列表
const loadMemberList = async () => {
  const res = await getAllMember()
  memberList.value = res.data || []
}

// 加载资产列表
const loadAssetsList = async () => {
  const res = await getAllAssets()
  assetsList.value = res.data || []
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = { memberId: '', assetsId: '' }
  pageNum.value = 1
  getStoreList()
}

// 分页事件
const handleSizeChange = (val) => {
  pageSize.value = val
  getStoreList()
}
const handleCurrentChange = (val) => {
  pageNum.value = val
  getStoreList()
}

// 计算总价（数量 * 单价）
const calcTotalPrice = () => {
  if (storeForm.value.quantity && storeForm.value.unitPrice) {
    const quantity = Number(storeForm.value.quantity)
    const unitPrice = Number(storeForm.value.unitPrice)
    storeForm.value.totalPrice = (quantity * unitPrice).toFixed(2)
  }
}

// 新增
const handleAdd = () => {
  dialogTitle.value = '新增购买记录'
  storeForm.value = {
    id: '',
    memberId: '',
    assetsId: '',
    quantity: 1,
    unitPrice: '',
    totalPrice: ''
  }
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑购买记录'
  storeForm.value = {
    ...row,
    unitPrice: row.unitPrice?.toString() || '',
    totalPrice: row.totalPrice?.toString() || ''
  }
  dialogVisible.value = true
}

// 删除
const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该购买记录？', '提示', { type: 'warning' })
    await deleteStore(id)
    ElMessage.success('删除成功')
    getStoreList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    // 提交前重新计算总价
    calcTotalPrice()
    await storeFormRef.value.validate()

    // 转换为BigDecimal格式
    const submitData = {
      ...storeForm.value,
      unitPrice: parseFloat(storeForm.value.unitPrice),
      totalPrice: parseFloat(storeForm.value.totalPrice)
    }

    if (storeForm.value.id) {
      await updateStore(submitData)
      ElMessage.success('修改成功')
    } else {
      await addStore(submitData)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getStoreList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出 Excel
const handleExport = async () => {
  try {
    const res = await exportStoreExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `商品购买记录表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
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
.store-page {
  height: 100%;
  padding: 16px;
}

/* 表格样式：白底黑字，与全局风格统一 */
:deep(.store-table) {
  background-color: #ffffff !important;
}
:deep(.store-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.store-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.store-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.store-table .el-table__row:hover .el-table__cell) {
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
:deep(.el-input__inner) {
  color: #000000 !important;
}
</style>