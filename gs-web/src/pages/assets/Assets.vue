<template>
  <div class="assets-page">
    <!-- 搜索和新增：新增ES分词查询按钮 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input
              v-model="searchForm.name"
              placeholder="输入关键词（支持ES分词查询）"
              style="width: 200px;"
              @keyup.enter="handleEsSearch"
          />
        </el-form-item>
        <el-form-item label="商品价格">
          <el-input-number v-model="searchForm.price" placeholder="请输入价格" style="width: 200px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getAssetsList">普通查询</el-button>
          <el-button type="primary" icon="Search" @click="handleEsSearch">ES分词查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增商品</el-button>
          <el-button type="success" icon="Download" @click="handleExport">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 商品表格 -->
    <el-card>
      <el-table v-loading="loading" :data="assetsList" border stripe style="width: 100%;" class="assets-table">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" min-width="150" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="picture" label="商品图片" width="120">
          <template #default="scope">
            <el-image :src="scope.row.picture" style="width: 60px; height: 60px" fit="cover" />
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="200" :formatter="formatDate" />
        <el-table-column prop="updated" label="修改时间" width="200" :formatter="formatDate" />
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页：普通查询显示，ES查询隐藏 -->
      <el-pagination
          v-show="!isEsSearch"
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="assetsFormRef" :model="assetsForm" :rules="assetsRules" label-width="120px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="assetsForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input-number v-model="assetsForm.price" min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input-number v-model="assetsForm.stock" min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="商品图片" prop="picture">
          <el-input v-model="assetsForm.picture" placeholder="请输入图片URL" />
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
import { getAssetsPage, addAssets, updateAssets, deleteAssets, searchAssetsByTitle, exportAssetsExcel } from '@/api/assets'
import dayjs from 'dayjs'

// ------------- 状态定义 -------------
const loading = ref(false)
const assetsList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)
const isEsSearch = ref(false) // 标记是否为ES查询

// 搜索表单
const searchForm = ref({
  name: '',
  price: null
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增商品')
const assetsFormRef = ref(null)
const assetsForm = ref({
  id: '',
  name: '',
  price: 0,
  stock: 0,
  picture: ''
})

// 校验规则
const assetsRules = ref({
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  price: [{ required: true, message: '请输入商品价格', trigger: 'blur' }],
  stock: [{ required: true, message: '请输入库存', trigger: 'blur' }]
})

// ------------- 工具方法 -------------
const formatDate = (row, column) => {
  if (!row[column.prop]) return '-'
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm:ss')
}

// ------------- 生命周期 -------------
onMounted(() => {
  getAssetsList()
})

// ------------- 核心API -------------
// 普通分页查询
const getAssetsList = async () => {
  try {
    loading.value = true
    isEsSearch.value = false // 标记为普通查询
    const res = await getAssetsPage({
      assets: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })

    assetsList.value = res.data?.records || res.data?.list || []
    total.value = res.data?.total || 0

    if (assetsList.value.length === 0) {
      ElMessage.info('暂无商品数据')
    }
  } catch (error) {
    ElMessage.error('获取商品失败：' + (error.msg || error.message))
    console.error(error)
  } finally {
    loading.value = false
  }
}

// ES分词查询
const handleEsSearch = async () => {
  if (!searchForm.value.name.trim()) {
    ElMessage.warning('请输入商品名称关键词！')
    return
  }
  try {
    loading.value = true
    isEsSearch.value = true // 标记为ES查询，隐藏分页
    const res = await searchAssetsByTitle(searchForm.value.name.trim())

    // ES查询结果直接赋值（不分页）
    assetsList.value = res.data || []
    total.value = assetsList.value.length

    ElMessage.success(`ES分词查询成功，共找到 ${total.value} 条结果`)
  } catch (error) {
    ElMessage.error('ES分词查询失败：' + (error.msg || error.message))
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = { name: '', price: null }
  pageNum.value = 1
  isEsSearch.value = false // 重置ES查询标记
  getAssetsList() // 重置后默认普通查询
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getAssetsList()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getAssetsList()
}

// ------------- 增删改查 -------------
const handleAdd = () => {
  dialogTitle.value = '新增商品'
  assetsForm.value = {
    id: '', name: '', price: 0, stock: 0, picture: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑商品'
  assetsForm.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除此商品？', '提示', { type: 'warning' })
    await deleteAssets({ id })
    ElMessage.success('删除成功')
    getAssetsList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleSubmit = async () => {
  try {
    await assetsFormRef.value.validate()
    if (assetsForm.value.id) {
      await updateAssets(assetsForm.value)
      ElMessage.success('修改成功')
    } else {
      await addAssets(assetsForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getAssetsList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// 导出Excel
const handleExport = async () => {
  try {
    const res = await exportAssetsExcel()
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = '商品表.xlsx'
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link)
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败：' + (error.msg || error.message))
  }
}
</script>

<style scoped>
.assets-page {
  height: 100%;
  padding: 16px;
}
.search-form { margin: 0; }
.el-tag { margin-right: 8px; }

/* ✅ 表格整体白底 */
:deep(.assets-table) {
  background-color: #ffffff !important;
}

/* ✅ 表格单元格：白底+黑字 */
:deep(.assets-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important; /* 文字改为黑色 */
  border-color: #e5e5e5 !important; /* 浅色边框 */
}

/* ✅ 表头：浅灰底+黑字 */
:deep(.assets-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}

/* ✅ 斑马纹行：稍浅白底 */
:deep(.assets-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}

/* ✅ hover行：浅灰底 */
:deep(.assets-table .el-table__row:hover .el-table__cell) {
  background-color: #f0f0f0 !important;
}

/* ✅ 分页组件：黑字 */
:deep(.el-pagination) {
  color: #000000 !important;
}

/* ✅ 卡片：白底 */
:deep(.el-card) {
  background-color: #ffffff !important;
  border-color: #e5e5e5 !important;
}
:deep(.el-card__header) {
  background-color: #fafafa !important;
  color: #000000 !important;
  border-bottom-color: #e5e5e5 !important;
}

/* ✅ 表单标签/输入框：黑字+白底 */
:deep(.el-form-item__label) {
  color: #000000 !important;
}
:deep(.el-input__wrapper), :deep(.el-input-number) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #dcdcdc !important;
}
:deep(.el-input__inner) {
  color: #000000 !important;
}
</style>