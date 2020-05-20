<template>
    <div class="manageUser-wrapper">
        <a-tabs>
            <a-tab-pane tab="账户管理" key="1">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button type="primary" @click="addManager"><a-icon type="plus" />添加酒店工作人员</a-button>
                    <a-divider type="vertical"></a-divider>
                    <a-button type="primary" @click="addMarketer"><a-icon type="plus" />添加网站营销人员</a-button>
                </div>
                <a-table
                    :columns="columns"
                    :dataSource="userList"
                    bordered
                >
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="userType" slot-scope="text">
                        <span v-if="text === 'Client'">客户</span>
                        <span v-if="text === 'HotelManager'">酒店工作人员</span>
                        <span v-if="text === 'Marketer'">网站营销人员</span>
                    </span>
                    <span slot="birthday" slot-scope="text">
                        <a-date-picker
                                :default-value="moment(text)"
                                disabled
                        />
                    </span>
                    <span slot="userType" slot-scope="text">
                        <a-tag v-if="text==='Client'" color="cyan">客户</a-tag>
                        <a-tag v-if="text==='HotelManager'" color="geekblue">酒店工作人员</a-tag>
                        <a-tag v-if="text==='Marketer'" color="pink">网站营销人员</a-tag>
                    </span>
                    <span slot="action" slot-scope="text, record">
                        <a-button type="danger" icon='delete' @click="order(record)">删除用户</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <AddManagerModal></AddManagerModal>
        <AddMarketerModal></AddMarketerModal>
    </div>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
const moment = require('moment')
import AddManagerModal from './components/addManagerModal'
import AddMarketerModal from './components/addMarketerModal'
const columns = [
    {
        title: '用户类型',
        dataIndex: 'userType',
        scopedSlots: { customRender: 'userType' },
        filters: [
            {
                text: '客户',
                value: 'Client',
            },
            {
                text: '酒店工作人员',
                value: 'HotelManager',
            },
            {
                text: '网站营销人员',
                value: 'Marketer',
            },
        ],
        filterMultiple: true,
        onFilter: (value, record) => record.userType === value,
    },
    {  
        title: '用户邮箱',
        dataIndex: 'email',
    },
    {  
        title: '用户名',
        dataIndex: 'userName',
    },
    {
        title: '用户密码',
        dataIndex: 'password',
    },
    {
        title: '用户手机号',
        dataIndex: 'phoneNumber',
    },
    {
        title: '生日',
        dataIndex: 'birthday',
        scopedSlots: { customRender: 'birthday' },
    },
    {
        title: '信用值',
        dataIndex: 'credit',
        scopedSlots: { customRender: 'credit' },
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
export default {
    name: 'manageUser',
    data(){
        return {
            formLayout: 'horizontal',
            pagination: {},
            columns,
            data: [],
            form: this.$form.createForm(this, { name: 'manageUser' }),
        }
    },
    components: {
        AddManagerModal,
        AddMarketerModal
    },
    computed: {
        ...mapGetters([
            'userInfo',
            'addManagerModalVisible',
            'addMarketerModalVisible',
            'userList'
        ])
    },
    async mounted() {
        await this.getUserList()
        await this.getUserInfo()
    },
    methods: {
        ...mapActions([
            'getUserList',
            'getUserInfo'
        ]),
        ...mapMutations([
            'set_addManagerModalVisible',
            'set_addMarketerModalVisible'
        ]),
        addManager(){
            this.set_addManagerModalVisible(true)
        },
        addMarketer(){
            this.set_addMarketerModalVisible(true)
        },
        order(record){

        },
        moment,
    }
}
</script>
<style scoped lang="less">
    .manageUser-wrapper {
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
    .manageUser-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">
    
</style>