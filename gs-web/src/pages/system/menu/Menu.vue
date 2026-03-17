<template>
  <div class="menu-page">
    <!-- 搜索和新增 -->
    <el-card class="search-card" style="margin-bottom: 20px;">
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="菜单名称">
          <el-input v-model="searchForm.name" placeholder="请输入菜单名称" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="菜单路径">
          <el-input v-model="searchForm.path" placeholder="请输入菜单路径" style="width: 200px;" />
        </el-form-item>
        <el-form-item label="菜单类型">
          <el-select v-model="searchForm.type" placeholder="请选择菜单类型" style="width: 180px;">
            <el-option label="目录" value="1" />
            <el-option label="菜单" value="2" />
            <el-option label="按钮" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getMenuList">查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增菜单</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 菜单表格（字段完全匹配数据库） -->
    <el-card>
      <el-table v-loading="loading" :data="menuList" border stripe style="width: 100%;">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="parentId" label="父菜单ID" width="100">
          <template #default="scope">
            {{ scope.row.parentId || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="菜单名称" min-width="150" />
        <el-table-column prop="path" label="菜单路径" min-width="200" />
        <el-table-column prop="component" label="组件路径" min-width="200" />
        <el-table-column prop="icon" label="菜单图标" width="100" />
        <el-table-column prop="type" label="类型" width="80">
          <template #default="scope">
            <el-tag :type="scope.row.type === '1' ? 'info' : (scope.row.type === '2' ? 'success' : 'danger')">
              {{ scope.row.type === '1' ? '目录' : (scope.row.type === '2' ? '菜单' : '按钮') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="hidden" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.hidden === 0 ? 'success' : 'danger'">
              {{ scope.row.hidden === 0 ? '显示' : '隐藏' }}
            </el-tag>
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

    <!-- 新增/编辑弹窗（包含所有字段） -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" destroy-on-close>
      <el-form ref="menuFormRef" :model="menuForm" :rules="menuRules" label-width="120px">
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="menuForm.name" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="menuForm.path" placeholder="请输入前端路由路径（如/system/menu）" />
        </el-form-item>
        <el-form-item label="组件路径" prop="component">
          <el-input v-model="menuForm.component" placeholder="请输入前端组件路径（如gym/menu/index）" />
        </el-form-item>
        <el-form-item label="菜单图标" prop="icon">
          <el-input v-model="menuForm.icon" placeholder="请输入图标名称（如Menu）" />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="menuForm.type">
            <el-radio label="1">目录</el-radio>
            <el-radio label="2">菜单</el-radio>
            <el-radio label="3">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="menuForm.sort" min="0" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="父菜单ID" prop="parentId">
          <el-input-number v-model="menuForm.parentId" min="0" placeholder="0表示一级菜单" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态" prop="hidden">
          <el-radio-group v-model="menuForm.hidden">
            <el-radio :label="0">显示</el-radio>
            <el-radio :label="1">隐藏</el-radio>
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
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMenuPage, addMenu, updateMenu, deleteMenu } from '@/api/menu'
import dayjs from 'dayjs'

// ------------- 状态定义 -------------
const loading = ref(false)
const menuList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const total = ref(0)

// 搜索表单
const searchForm = ref({
  name: '',
  path: '',
  type: ''
})

// 弹窗表单
const dialogVisible = ref(false)
const dialogTitle = ref('新增菜单')
const menuFormRef = ref(null)
const menuForm = ref({
  id: '',
  parentId: 0,
  name: '',
  path: '',
  component: '',
  icon: '',
  type: '2', // 默认菜单
  sort: 0,
  hidden: 0
})

// 校验规则
const menuRules = ref({
  name: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  path: [{ required: true, message: '请输入菜单路径', trigger: 'blur' }]
})

// ------------- 工具方法 -------------
// 时间格式化
const formatDate = (row, column) => {
  if (!row[column.prop]) return '-'
  return dayjs(row[column.prop]).format('YYYY-MM-DD HH:mm:ss')
}

// ------------- 生命周期 -------------
onMounted(() => {
  getMenuList()
})

// ------------- 核心API -------------
const getMenuList = async () => {
  try {
    loading.value = true
    // ✅ 关键修改：把 pageNum/pageSize 改成 page/size，对齐后端DTO字段名
    const res = await getMenuPage({
      menu: { ...searchForm.value },
      page: pageNum.value,    // 原pageNum → 改为page
      size: pageSize.value    // 原pageSize → 改为size
    })

    console.log('【菜单数据】', res.data) // 打印数据排查

    // 兼容 records 和 list 两种返回格式（适配MyBatis-Plus的分页返回）
    menuList.value = res.data?.records || res.data?.list || []
    total.value = res.data?.total || 0

    if (menuList.value.length === 0) {
      ElMessage.info('暂无菜单数据')
    }
  } catch (error) {
    ElMessage.error('获取菜单失败：' + (error.msg || error.message))
    console.error(error)
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = { name: '', path: '', type: '' }
  getMenuList()
}

const handleSizeChange = (val) => {
  pageSize.value = val
  getMenuList()
}

const handleCurrentChange = (val) => {
  pageNum.value = val
  getMenuList()
}

// ------------- 增删改查 -------------
const handleAdd = () => {
  dialogTitle.value = '新增菜单'
  menuForm.value = {
    id: '', parentId: 0, name: '', path: '', component: '', icon: '', type: '2', sort: 0, hidden: 0
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑菜单'
  menuForm.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = async (id) => {
  if (!id) return
  try {
    await ElMessageBox.confirm('确认删除此菜单？', '提示', { type: 'warning' })
    await deleteMenu(id)
    ElMessage.success('删除成功')
    getMenuList()
  } catch (error) {
    if (error !== 'cancel') ElMessage.error('删除失败')
  }
}

const handleSubmit = async () => {
  try {
    await menuFormRef.value.validate()
    if (menuForm.value.id) {
      await updateMenu(menuForm.value)
      ElMessage.success('修改成功')
    } else {
      await addMenu(menuForm.value)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getMenuList()
  } catch (error) {
    if (error.name !== 'ValidationError') {
      ElMessage.error(error.msg || '操作失败')
    }
  }
}
</script>

<style scoped>
.menu-page { height: 100%; padding: 16px; }
.search-form { margin: 0; }
.el-tag { margin-right: 8px; }
</style>