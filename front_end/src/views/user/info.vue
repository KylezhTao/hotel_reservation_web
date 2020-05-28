<template>
    <div class="info-wrapper">
        <a-tabs>
            <a-tab-pane tab="我的信息" key="1">
                <a-form :form="form" style="margin-top: 30px">
                    
                    <a-form-item label="用户名" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1  }">
                        <a-input
                            placeholder="请填写用户名"
                            v-decorator="['userName', { rules: [{ required: true, message: '请输入用户名' }] }]"
                            v-if="modify"
                        />
                        <span v-else>{{ userInfo.userName }}</span>
                    </a-form-item>
                    <a-form-item label="邮箱" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <span>{{ userInfo.email }}</span>
                    </a-form-item>
                    
                    <a-form-item label="手机号" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <a-input
                            placeholder="请填写手机号"
                            v-decorator="['phoneNumber', { rules: [{ required: true, message: '请输入手机号' }] }]"
                            v-if="modify"
                        />
                        <span v-else>{{ userInfo.phoneNumber}}</span>
                    </a-form-item>
                    <a-form-item label="生日" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <a-date-picker
                                :default-value="moment(userInfo.birthday)"
                                v-if="modify"
                                v-decorator="['birthday', { rules: [{ required: false}] }]"
                                disabled
                        />
                        <span v-else>{{ userInfo.birthday}}</span>
                    </a-form-item>
                    <a-form-item label="信用值" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <span>{{ userInfo.credit }}</span>
                    </a-form-item>
                    <a-form-item label="公司名" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }">
                        <span>{{ userInfo.companyName }}</span>
                    </a-form-item>
                    <a-form-item v-if="modify" :label-col="{span: 4}" :wrapper-col="{span: 8, offset: 4}">
                        <a-checkbox :checked="checkPassword" @change="handleChange">
                            若想修改密码，请勾选此项
                        </a-checkbox>
                    </a-form-item>
                    <a-form-item label="新密码" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }" v-if="checkPassword && modify">
                        <a-input-password allow-clear
                            placeholder="请输入新密码"
                            v-decorator="['password', {
                                rules: [
                                    { required: checkPassword, message: '请输入新密码' },
                                    { validator: validateToNextPassword },
                                ]
                            }]"
                        />
                    </a-form-item>
                    <a-form-item label="确认新密码" :label-col="{ span: 3 }" :wrapper-col="{ span: 8, offset: 1 }" v-if="checkPassword && modify" has-feedback>
                        <a-input-password allow-claer
                                placeholder="请确认新密码"
                                v-decorator="['confirm', {
                                    rules: [
                                        { required: checkPassword, message: '请确认新密码' },
                                        { validator: compareToFirstPassword },
                                    ]
                                }]"
                                @blur="handleConfirmBlur"
                        />
                    </a-form-item>
                    <a-form-item :wrapper-col="{ span: 12, offset: 5 }" v-if="modify">
                        <a-button type="primary" @click="saveModify">
                            保存
                        </a-button>
                        <a-button type="default" style="margin-left: 30px" @click="cancelModify">
                            取消
                        </a-button>
                    </a-form-item>
                     <a-form-item :wrapper-col="{ span: 8, offset: 4 }" v-else>
                        <a-button type="primary" @click="modifyInfo">
                            修改信息
                        </a-button>
                    </a-form-item>
                </a-form>
            </a-tab-pane>
            <a-tab-pane tab="我的订单" key="2">
                <a-table
                    :columns="columns"
                    :dataSource="userOrderList"
                    class="components-table-demo-nested"
                    bordered
                >
                    <span slot="expandedRowRender" slot-scope="record" style="margin: 0">
                        <a-tag>- 生成时间： {{ record.createDate }}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 入住日期： {{ record.checkInDate }}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 退房日期： {{record.checkOutDate}}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 订单最晚执行时间： {{record.latestExecTime}}</a-tag>
                    </span>
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="roomType" slot-scope="text">
                        <a-tag v-if="text === 'BigBed'">大床房</a-tag>
                        <a-tag v-if="text === 'DoubleBed'">双床房</a-tag>
                        <a-tag v-if="text === 'Family'">家庭房</a-tag>
                    </span>
                    <span slot="haveChild" slot-scope="text">
                        <span v-if="text === true">有</span>
                        <span v-if="text === false">无</span>
                    </span>
                    <a-tag slot="orderState" :color="text==='已预订'?'#108ee9':text==='已执行'?'#87d068':text==='已撤销'?'#f6bb03':'#ff0000'" slot-scope="text">
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">
                        <a-popconfirm
                            title="你确定撤销该笔订单吗？"
                            @confirm="confirmCancelOrder(record.id)"
                            @cancel="cancelCancelOrder"
                            okText="确定"
                            cancelText="取消"
                            v-if="record.orderState === '已预订'"
                        >
                            <a-button type="danger" icon="undo" size="small">撤销</a-button>
                        </a-popconfirm>
                        <a-button type="primary" icon="edit" size="small"
                                  v-if="record.orderState === '已执行'"
                                  @click.native="jumpToDetails(record.hotelId)"
                        >
                            评价
                        </a-button>
                        
                    </span>
                </a-table>
            </a-tab-pane>

            <a-tab-pane v-if='userInfo.userType==="Client"' tab="信用记录" key="3">
                <a-table
                        :columns="columns2"
                        :dataSource="userCreditRecordList"
                        class="components-table-demo-nested"
                        bordered
                >
                    <span slot="datetime" slot-scope="text">
                        <a-tag >{{ text }}</a-tag>
                    </span>
                    <span slot="action" slot-scope="text">
                        <a-tag v-if="text==='充值'" :color="'#108ee9'" ><a-icon type="money-collect"/> {{ text }}</a-tag>
                        <a-tag v-if="text==='订单撤销'" :color="'#e96e10'" ><a-icon type="undo"/> {{ text }}</a-tag>
                        <a-tag v-if="text==='订单执行'" :color="'#28ac03'" ><a-icon type="check"/> {{ text }}</a-tag>
                        <a-tag v-if="text==='订单异常'" :color="'#fd045b'" ><a-icon type="question"/> {{ text }}</a-tag>
                    </span>
                    <span slot="change" slot-scope="text">
                        <a-tag :color="text >= 0?'#27c405':'#f82d2d'" >{{ text>=0?"+ "+text:"- " + -text }}</a-tag>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
const moment = require('moment')
const columns = [
    {  
        title: '订单号',
        dataIndex: 'id',
    },
    {  
        title: '酒店名',
        dataIndex: 'hotelName',
    },
    {
        title: '房型',
        dataIndex: 'roomType',
        scopedSlots: { customRender: 'roomType' }
    },
    {
        title: '数量',
        dataIndex: 'roomNum',
    },
    {
        title: '预计入住人数',
        dataIndex: 'peopleNum',
    },
    {
        title: '有无儿童',
        dataIndex: 'haveChild',
        scopedSlots: { customRender: 'haveChild' }
    },
    {
        title: '房价',
        dataIndex: 'price',
    },
    {
        title: '状态',
        filters: [{ text: '已预订', value: '已预订' }, { text: '已撤销', value: '已撤销' }, { text: '已执行', value: '已执行' }],
        onFilter: (value, record) => record.orderState.includes(value),
        dataIndex: 'orderState',
        scopedSlots: { customRender: 'orderState' }
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
    
  ];
const columns2 = [
    {
        title: '记录号',
        dataIndex: 'id',
    },
    {
        title: '时间',
        dataIndex: 'datetime',
        scopedSlots: { customRender: 'datetime' }
    },
    {
        title: '订单号',
        dataIndex: 'orderId',
    },
    {
        title: '动作',
        dataIndex: 'action',
        scopedSlots: { customRender: 'action' }
    },
    {
        title: '信用值变化',
        dataIndex: 'change',
        scopedSlots: { customRender: 'change' }
    },
    {
        title: '信用值结果',
        dataIndex: 'result',
        scopedSlots: { customRender: 'result' }
    },
]
export default {
    name: 'info',
    data(){
        return {
            checkPassword: false,
            confirmDirty: false,
            modify: false,
            formLayout: 'horizontal',
            pagination: {},
            columns,
            columns2,
            data: [],
            form: this.$form.createForm(this, { name: 'coordinated' }),
        }
    },
    components: {
    },
    computed: {
        ...mapGetters([
            'userId',
            'userInfo',
            'userOrderList',
            'userCreditRecordList'
        ])
    },
    async mounted() {
        await this.getUserInfo()
        await this.getUserOrders()
        await this.getUserCreditRecords()
    },
    methods: {
        ...mapActions([
            'getUserInfo',
            'getUserOrders',
            'updateUserInfo',
            'cancelOrder',
            'getUserCreditRecords'
        ]),
        moment,
        handleChange(e) {
            this.checkPassword = e.target.checked;
            this.$nextTick(() => {
                this.form.validateFields(['password'], { force: true });
            });
        },
        handleConfirmBlur(e) {
            const value = e.target.value;
            this.confirmDirty = this.confirmDirty || !!value;
        },
        compareToFirstPassword(rule, value, callback) {
            const form = this.form;
            if (value && value !== form.getFieldValue('password')) {
                callback('两次密码不一致');
            } else {
                callback();
            }
        },
        validateToNextPassword(rule, value, callback) {
            const form = this.form;
            if (value && this.confirmDirty) {
                form.validateFields(['confirm'], { force: true });
            }
            callback();
        },
        saveModify() {
            this.form.validateFields((err, values) => {
                if (!err) {
                    if(this.checkPassword){
                        const data = {
                            userName: this.form.getFieldValue('userName'),
                            phoneNumber: this.form.getFieldValue('phoneNumber'),
                            password: this.form.getFieldValue('password')
                        }
                        this.updateUserInfo(data).then( ()=>{
                            this.modify = false
                        })
                    }else{
                        const data = {
                            userName: this.form.getFieldValue('userName'),
                            phoneNumber: this.form.getFieldValue('phoneNumber'),
                        }
                        this.updateUserInfo(data).then( ()=>{
                            this.modify = false
                        })
                    }
                }
            });
        },
        modifyInfo() {
            setTimeout(() => {
                this.form.setFieldsValue({
                    'userName': this.userInfo.userName,
                    'phoneNumber': this.userInfo.phoneNumber,
                    'birthday': this.userInfo.birthday,
                    'companyName': this.userInfo.companyName,
                })
            }, 0)
            this.modify = true
        },
        cancelModify() {
            this.modify = false
        },
        confirmCancelOrder(orderId){
            this.cancelOrder(orderId)
        },
        cancelCancelOrder() {

        },
        jumpToDetails(id){
            this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
        },
        
    }
}

</script>
<style scoped lang="less">
    .info-wrapper {
        padding: 50px;
        .chart {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 20px
        }
    }
</style>
<style lang="less">
    .info-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">
    
</style>