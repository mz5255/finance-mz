<template>
  <div class="app-container excel-style">
    <!-- Excel风格工具栏 -->
    <div class="excel-toolbar">
      <el-row :gutter="10" class="mb8">
        <el-col :span="6">
          <el-input
            v-model="excelSearch.keyword"
            placeholder="输入关键词搜索..."
            clearable
            prefix-icon="el-icon-search"
            size="small"
            @keyup.enter.native="handleExcelSearch"
            @clear="handleExcelSearch">
            <el-dropdown slot="prepend" @command="handleSearchFieldChange" size="small">
              <el-button size="small">
                {{ excelSearch.fieldLabel }}<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="all">全部字段</el-dropdown-item>
                <el-dropdown-item command="senderName">发货方</el-dropdown-item>
                <el-dropdown-item command="cargoName">货物名称</el-dropdown-item>
                <el-dropdown-item command="licensePlate">车牌号</el-dropdown-item>
                <el-dropdown-item command="driverName">司机姓名</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </el-input>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['system:data:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['system:data:edit']"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['system:data:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            plain
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['system:data:export']"
          >导出</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            plain
            icon="el-icon-refresh"
            size="mini"
            @click="resetExcelSearch"
          >重置</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
    </div>

    <!-- Excel风格表格 -->
    <el-table
      v-loading="loading"
      :data="dataList"
      @selection-change="handleSelectionChange"
      :row-class-name="tableRowClassName"
      border
      height="calc(100vh - 250px)"
      size="mini"
      stripe>

      <el-table-column type="selection" width="40" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />

      <!-- 发货信息 -->
      <el-table-column label="发货信息" align="center">
        <el-table-column label="发货日期" align="center" prop="shippingDate" width="100">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.shippingDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="发货方" align="center" prop="senderName" width="120" />
        <el-table-column label="联系电话" align="center" prop="senderPhone" width="120" />
        <el-table-column label="装货地址" align="center" prop="loadingAddress" width="150" show-overflow-tooltip />
      </el-table-column>

      <!-- 货物信息 -->
      <el-table-column label="货物信息" align="center">
        <el-table-column label="货物名称" align="center" prop="cargoName" width="120" />
        <el-table-column label="吨位数" align="center" prop="weight" width="80" />
        <el-table-column label="支数" align="center" prop="packagesCount" width="60" />
        <el-table-column label="件数" align="center" prop="itemsCount" width="60" />
      </el-table-column>

      <!-- 运输信息 -->
      <el-table-column label="运输信息" align="center">
        <el-table-column label="司机" align="center" prop="driverName" width="100" />
        <el-table-column label="车牌号" align="center" prop="licensePlate" width="100" />
        <el-table-column label="司机电话" align="center" prop="driverPhone" width="120" />
        <el-table-column label="卸货地址" align="center" prop="unloadingAddress" width="150" show-overflow-tooltip />
      </el-table-column>

      <!-- 费用信息 -->
      <el-table-column label="费用信息" align="center">
        <el-table-column label="中转费" align="center" prop="transferFee" width="80" />
        <el-table-column label="车总价" align="center" prop="carTotalPrice" width="80" />
        <el-table-column label="利润" align="center" prop="profit" width="80" />
      </el-table-column>

      <!-- 时间信息 -->
      <el-table-column label="时间信息" align="center">
        <el-table-column label="创建时间" align="center" prop="createTime" width="100">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="修改时间" align="center" prop="updateTime" width="100">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" align="center" width="120" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:data:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:data:remove']"
          >删除</el-button>
          <el-dropdown @command="handleColorCommand" size="mini">
            <el-button type="text" size="mini">颜色</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item :command="{row: scope.row, color: 'default'}">默认</el-dropdown-item>
              <el-dropdown-item :command="{row: scope.row, color: 'success'}">绿色</el-dropdown-item>
              <el-dropdown-item :command="{row: scope.row, color: 'warning'}">黄色</el-dropdown-item>
              <el-dropdown-item :command="{row: scope.row, color: 'danger'}">红色</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" size="small">
        <el-row>
          <el-col :span="12">
            <el-form-item label="发货日期" prop="shippingDate">
              <el-date-picker clearable
                v-model="form.shippingDate"
                type="date"
                value-format="yyyy-MM-dd"
                placeholder="请选择发货日期"
                style="width: 100%">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发货方" prop="senderName">
              <el-input v-model="form.senderName" placeholder="请输入发货方" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="senderPhone">
              <el-input v-model="form.senderPhone" placeholder="请输入发货方联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="装货地址" prop="loadingAddress">
              <el-input v-model="form.loadingAddress" placeholder="请输入装货地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="货物名称" prop="cargoName">
              <el-input v-model="form.cargoName" placeholder="请输入货物名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="吨位数" prop="weight">
              <el-input v-model="form.weight" placeholder="请输入吨位数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="支数" prop="packagesCount">
              <el-input v-model="form.packagesCount" placeholder="请输入支数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="件数" prop="itemsCount">
              <el-input v-model="form.itemsCount" placeholder="请输入件数" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="司机姓名" prop="driverName">
              <el-input v-model="form.driverName" placeholder="请输入司机姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车牌号" prop="licensePlate">
              <el-input v-model="form.licensePlate" placeholder="请输入车牌号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="司机电话" prop="driverPhone">
              <el-input v-model="form.driverPhone" placeholder="请输入司机联系电话" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="卸货地址" prop="unloadingAddress">
              <el-input v-model="form.unloadingAddress" placeholder="请输入卸货地址" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="中转费" prop="transferFee">
              <el-input v-model="form.transferFee" placeholder="请输入中转费" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车总价" prop="carTotalPrice">
              <el-input v-model="form.carTotalPrice" placeholder="请输入车总价" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="利润" prop="profit">
              <el-input v-model="form.profit" placeholder="请输入利润" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="备注" prop="remarks">
              <el-input v-model="form.remarks" type="textarea" placeholder="请输入备注" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" size="small">确 定</el-button>
        <el-button @click="cancel" size="small">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listData, getData, delData, addData, updateData } from "@/api/service/finance"

export default {
  name: "Data",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      dataList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // Excel风格搜索
      excelSearch: {
        keyword: '',
        field: 'all',
        fieldLabel: '全部字段'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 20,
        shippingDate: null,
        senderName: null,
        senderPhone: null,
        loadingAddress: null,
        cargoName: null,
        weight: null,
        packagesCount: null,
        itemsCount: null,
        unloadingAddress: null,
        unloadingContactName: null,
        unloadingContactPhone: null,
        driverName: null,
        licensePlate: null,
        driverPhone: null,
        transferPoint: null,
        transferFee: null,
        infoFee: null,
        shippingInfoFee: null,
        tax: null,
        receipt: null,
        collectOnDelivery: null,
        refund: null,
        managementFee: null,
        craneFee: null,
        highwayFee: null,
        pickupFee: null,
        fuel: null,
        carMiscFee: null,
        carUnitPrice: null,
        carTotalPrice: null,
        price: null,
        priceTotal: null,
        shipmentFeeContactName: null,
        profit: null,
        remarks: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        isDelete: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shippingDate: [
          { required: true, message: "发货日期不能为空", trigger: "blur" }
        ],
        senderName: [
          { required: true, message: "发货方不能为空", trigger: "blur" }
        ],
        senderPhone: [
          { required: true, message: "发货方联系电话不能为空", trigger: "blur" }
        ],
        loadingAddress: [
          { required: true, message: "装货地址不能为空", trigger: "blur" }
        ],
        cargoName: [
          { required: true, message: "货物名称不能为空", trigger: "blur" }
        ],
        weight: [
          { required: true, message: "吨位数不能为空", trigger: "blur" }
        ],
        packagesCount: [
          { required: true, message: "支数不能为空", trigger: "blur" }
        ],
        itemsCount: [
          { required: true, message: "件数不能为空", trigger: "blur" }
        ],
        unloadingAddress: [
          { required: true, message: "卸货地址不能为空", trigger: "blur" }
        ],
        driverName: [
          { required: true, message: "司机姓名不能为空", trigger: "blur" }
        ],
        licensePlate: [
          { required: true, message: "车牌号不能为空", trigger: "blur" }
        ],
        driverPhone: [
          { required: true, message: "司机联系电话不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listData(this.queryParams).then(response => {
        this.dataList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        shippingDate: null,
        senderName: null,
        senderPhone: null,
        loadingAddress: null,
        cargoName: null,
        weight: null,
        packagesCount: null,
        itemsCount: null,
        unloadingAddress: null,
        unloadingContactName: null,
        unloadingContactPhone: null,
        driverName: null,
        licensePlate: null,
        driverPhone: null,
        transferPoint: null,
        transferFee: null,
        infoFee: null,
        shippingInfoFee: null,
        tax: null,
        receipt: null,
        collectOnDelivery: null,
        refund: null,
        managementFee: null,
        craneFee: null,
        highwayFee: null,
        pickupFee: null,
        fuel: null,
        carMiscFee: null,
        carUnitPrice: null,
        carTotalPrice: null,
        price: null,
        priceTotal: null,
        shipmentFeeContactName: null,
        profit: null,
        remarks: null,
        createTime: null,
        updateTime: null,
        createBy: null,
        updateBy: null,
        isDelete: null
      }
      this.resetForm("form")
    },
    /** Excel风格搜索 */
    handleExcelSearch() {
      // 重置查询参数
      this.resetQueryParams()

      if (this.excelSearch.keyword) {
        if (this.excelSearch.field === 'all') {
          // 全字段搜索
          this.queryParams.senderName = this.excelSearch.keyword
          this.queryParams.cargoName = this.excelSearch.keyword
          this.queryParams.licensePlate = this.excelSearch.keyword
          this.queryParams.driverName = this.excelSearch.keyword
        } else {
          // 指定字段搜索
          this.queryParams[this.excelSearch.field] = this.excelSearch.keyword
        }
      }

      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置Excel搜索 */
    resetExcelSearch() {
      this.excelSearch = {
        keyword: '',
        field: 'all',
        fieldLabel: '全部字段'
      }
      this.resetQueryParams()
      this.getList()
    },
    /** 搜索字段变更 */
    handleSearchFieldChange(command) {
      const fieldMap = {
        'all': '全部字段',
        'senderName': '发货方',
        'cargoName': '货物名称',
        'licensePlate': '车牌号',
        'driverName': '司机姓名'
      }

      this.excelSearch.field = command
      this.excelSearch.fieldLabel = fieldMap[command]
    },
    /** 重置查询参数 */
    resetQueryParams() {
      Object.keys(this.queryParams).forEach(key => {
        if (key !== 'pageNum' && key !== 'pageSize') {
          this.queryParams[key] = null
        }
      })
    },
    /** 表格行样式 */
    tableRowClassName({row, rowIndex}) {
      // 根据行数据中的标记字段设置颜色
      if (row.rowColor) {
        return `row-${row.rowColor}`
      }
      // 根据利润自动着色
      if (row.profit !== null && row.profit !== undefined) {
        if (row.profit < 0) {
          return 'row-danger' // 亏损标红
        } else if (row.profit < 1000) {
          return 'row-warning' // 利润较低标黄
        } else {
          return 'row-success' // 利润较高标绿
        }
      }
      return ''
    },
    /** 处理颜色命令 */
    handleColorCommand(command) {
      const { row, color } = command
      // 这里可以调用API保存颜色设置
      this.$set(row, 'rowColor', color === 'default' ? '' : color)
      this.$message.success(`已标记为${color === 'default' ? '默认' : color}颜色`)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加运输信息"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getData(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改运输信息"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addData(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除运输信息编号为"' + ids + '"的数据项？').then(function() {
        return delData(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/data/export', {
        ...this.queryParams
      }, `运输数据_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.excel-style {
  padding: 10px;
}

.excel-toolbar {
  margin-bottom: 10px;
}

/* 表格行颜色样式 */
.el-table ::v-deep .row-success {
  background-color: #f0f9eb;
}

.el-table ::v-deep .row-warning {
  background-color: #fdf6ec;
}

.el-table ::v-deep .row-danger {
  background-color: #fef0f0;
}

/* 紧凑型表格样式 */
.el-table ::v-deep .el-table__header th {
  background-color: #f5f7fa;
  color: #606266;
  font-weight: 600;
  padding: 6px 0;
}

.el-table ::v-deep .el-table__body td {
  padding: 4px 0;
}

.el-table ::v-deep .el-table__row:hover {
  background-color: #f5f7fa !important;
}

/* 操作按钮样式 */
.el-table ::v-deep .el-button--text {
  padding: 2px;
}
</style>
