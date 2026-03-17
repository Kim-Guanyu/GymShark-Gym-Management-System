<template>
  <div class="coach-page">
    <!-- 搜索和新增 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="教练姓名">
          <el-input v-model="searchForm.name" placeholder="请输入教练姓名" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="教练性别">
          <el-select v-model="searchForm.sex" placeholder="请选择性别" style="width: 200px;">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="擅长项目">
          <el-input v-model="searchForm.specialty" placeholder="请输入擅长项目" style="width: 200px;" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getCoachList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增教练</el-button>
          <el-button type="success" icon="Download" @click="handleExportExcel">导出Excel</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 教练表格 -->
    <el-card>
      <el-table
          v-loading="loading"
          :data="coachList"
          border
          stripe
          class="coach-table"
          style="width: 100%;"
      >
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="coachNo" label="教练编号" width="140" />
        <el-table-column prop="name" label="教练姓名" min-width="100" />
        <el-table-column prop="sex" label="性别" width="80">
          <template #default="scope">
            <!-- 实体类sex：1=男 2=女 -->
            {{ scope.row.sex === '1' ? '男' : scope.row.sex === '2' ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="教练等级" width="100" />
        <el-table-column prop="specialty" label="擅长课程" min-width="120" />
        <el-table-column prop="phone" label="手机号" min-width="110" />
        <el-table-column prop="picture" label="教练头像" width="100">
          <template #default="scope">
            <el-image
                :src="scope.row.picture || 'https://picsum.photos/60/60'"
                style="width: 40px; height: 40px"
                fit="cover"
            >
              <template #error>
                <div style="display:flex;align-items:center;justify-content:center;width:100%;height:100%;background:#f5f5f5;color:#999;">
                  无图
                </div>
              </template>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="created" label="创建时间" width="160" :formatter="formatDate" />
        <el-table-column prop="updated" label="修改时间" width="160" :formatter="formatDate" />
        <el-table-column label="操作" width="120" fixed="right">
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
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="coachFormRef" :model="coachForm" :rules="coachRules" label-width="120px">
        <el-form-item label="教练编号" prop="coachNo">
          <el-input v-model="coachForm.coachNo" placeholder="请输入教练编号" />
        </el-form-item>
        <el-form-item label="教练姓名" prop="name">
          <el-input v-model="coachForm.name" placeholder="请输入教练姓名" />
        </el-form-item>
        <el-form-item label="登录密码" prop="password">
          <el-input v-model="coachForm.password" type="password" placeholder="请输入登录密码" />
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="coachForm.sex" placeholder="请选择性别">
            <el-option label="男" value="1" />
            <el-option label="女" value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="教练等级" prop="level">
          <el-input v-model="coachForm.level" placeholder="请输入教练等级（如：高级/中级）" />
        </el-form-item>
        <el-form-item label="擅长项目" prop="specialty">
          <el-input v-model="coachForm.specialty" placeholder="请输入擅长项目（如：健身、瑜伽、拳击）" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="coachForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="教练头像">
          <el-upload
              class="avatar-uploader"
              action="#"
              :show-file-list="false"
              :before-upload="beforeUploadImage"
              :on-success="handleUploadSuccess"
          >
            <!-- 修复v-if/v-else语法错误 -->
            <template v-if="coachForm.picture">
              < img :src="coachForm.picture" class="avatar" />
            </template>
            <template v-else>
              <el-icon class="avatar-uploader-icon"><Plus /></el-icon>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>

    <!-- 图片预览弹窗 -->
    <el-dialog v-model="previewVisible" title="教练头像预览" width="400px">
      < img :src="previewImageUrl" style="width: 100%;" />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Download } from '@element-plus/icons-vue'
import {
  getCoachPage, addCoach, updateCoach, deleteCoach,
  exportCoachExcel, uploadCoachImage,
  previewCoachImage
} from '@/api/coach'
import dayjs from 'dayjs'

// ------------- 状态定义 -------------
const loading = ref(false)
const coachList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单（对齐实体类字段）
const searchForm = ref({
  name: '',        // 教练姓名
  sex: '',         // 性别 1男 2女
  specialty: '',   // 擅长课程
  phone: ''        // 手机号
})

// 弹窗表单（完全对齐实体类）
const dialogVisible = ref(false)
const previewVisible = ref(false)
const previewImageUrl = ref('')
const dialogTitle = ref('新增教练')
const coachFormRef = ref(null)
const coachForm = ref({
  id: '',
  coachNo: '',     // 教练编号
  name: '',        // 教练姓名
  password: '',    // 登录密码
  phone: '',       // 手机号
  sex: '',         // 性别 1男 2女
  picture: '',     // 头像地址
  level: '',       // 教练等级
  specialty: '',   // 特长
  deleted: 0,      // 删除状态
  created: '',
  updated: ''
})

// 校验规则（对齐实体类字段）
const coachRules = ref({
  coachNo: [{ required: true, message: '请输入教练编号', trigger: 'blur' }],
  name: [{ required: true, message: '请输入教练姓名', trigger: 'blur' }],
  sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{
    required: true,
    message: '请输入手机号',
    trigger: 'blur',
    pattern: /^1[3-9]\d{9}$/
  }],
  specialty: [{ required: true, message: '请输入擅长项目', trigger: 'blur' }]
})

// ------------- 工具方法 -------------
const formatDate = (row, column) => {
  if (!row[column.prop]) return '-'
  // 适配LocalDateTime格式
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm:ss')
}

// ------------- 生命周期 -------------
onMounted(() => {
  getCoachList()
})

// ------------- 核心API -------------
const getCoachList = async () => {
  try {
    loading.value = true
    const res = await getCoachPage({
      coach: { ...searchForm.value },
      page: pageNum.value,
      size: pageSize.value
    })

    let data = res.data?.records || res.data?.list || res.data || []
    coachList.value = data.filter(item => item != null)
    total.value = res.data?.total || 0

    if (coachList.value.length === 0) {
      ElMessage.info('暂无教练数据')
    }
  } catch (error) {
    ElMessage.error('获取教练数据失败：' + (error.msg || error.message))
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = {
    name: '',
    sex: '',
    specialty: '',
    phone: ''
  }
  pageNum.value = 1
  getCoachList()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getCoachList()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getCoachList()
}

// ------------- 增删改查 -------------
const handleAdd = () => {
  dialogTitle.value = '新增教练'
  coachForm.value = {
    id: '',
    coachNo: '',
    name: '',
    password: '',
    phone: '',
    sex: '',
    picture: '',
    level: '',
    specialty: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑教练'
  coachForm.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除该教练？', '提示', { type: 'warning' })
    await deleteCoach({ id })
    ElMessage.success('删除成功')
    getCoachList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleSubmit = async () => {
  try {
    await coachFormRef.value.validate()
    if (coachForm.value.id) {
      await updateCoach(coachForm.value)
      ElMessage.success('修改成功')
    } else {
      await addCoach(coachForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getCoachList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}

// ------------- Excel导出 -------------
const handleExportExcel = async () => {
  try {
    const res = await exportCoachExcel()
    // 创建下载链接
    const blob = new Blob([res], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const a = document.createElement('a')
    a.href = url
    a.download = `教练表_${dayjs().format('YYYYMMDDHHmmss')}.xlsx`
    a.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('Excel导出成功')
  } catch (error) {
    ElMessage.error('Excel导出失败：' + error.message)
  }
}

// ------------- 图片上传/预览 -------------
const beforeUploadImage = (file) => {
  // 限制文件类型和大小
  const isImage = file.type.startsWith('image/')
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isImage) {
    ElMessage.error('只能上传图片格式文件！')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB！')
    return false
  }

  // 模拟上传（实际项目中替换为真实的文件上传逻辑）
  const objectName = `coach_${Date.now()}_${file.name}`
  uploadCoachImage({
    localFilePath: file.name,
    objectName
  }).then(res => {
    if (res.code === 200) {
      coachForm.value.picture = res.data
      ElMessage.success('头像上传成功')
    }
  })

  return false // 阻止默认上传
}

const handleUploadSuccess = (response) => {
  // 上传成功回调
}

const handlePreviewImage = (pictureUrl) => {
  if (!pictureUrl) {
    ElMessage.warning('暂无头像可预览')
    return
  }
  // 解析objectName（根据实际URL格式调整）
  const objectName = pictureUrl.split('/').pop()
  previewCoachImage(objectName).then(res => {
    previewImageUrl.value = res.data
    previewVisible.value = true
  })
}
</script>

<style scoped>
.coach-page {
  height: 100%;
  padding: 16px;
}
.search-form { margin: 0; }
.el-tag { margin-right: 8px; }

/* 表格样式：白底黑字 */
:deep(.coach-table) {
  background-color: #ffffff !important;
  overflow-x: auto; /* 横向滚动 */
}
:deep(.coach-table .el-table__cell) {
  background-color: #ffffff !important;
  color: #000000 !important;
  border-color: #e5e5e5 !important;
}
:deep(.coach-table .el-table__header .el-table__cell) {
  background-color: #f5f5f5 !important;
  color: #000000 !important;
  font-weight: bold;
}
:deep(.coach-table .el-table__row--striped .el-table__cell) {
  background-color: #fafafa !important;
}
:deep(.coach-table .el-table__row:hover .el-table__cell) {
  background-color: #f0f0f0 !important;
}

/* 分页/卡片/表单样式 */
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

/* 头像上传样式 */
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 120px;
  height: 120px;
}
.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
</style>