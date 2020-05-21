<template>
    <div class="manageClient-wrapper">
        <a-tabs>
            <a-tab-pane tab="信用充值" key="1">
                <a-table
                        :columns="columns"
                        :dataSource="clientList"
                        bordered
                >
                    <span slot="birthday" slot-scope="text">
                        <a-date-picker
                                :default-value="moment(text)"
                                disabled
                        />
                    </span>
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small" icon="money-collect"
                                  @click="rechargeCredit(record)">信用充值</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <RechargeCreditModal></RechargeCreditModal>
    </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import RechargeCreditModal from './components/rechargeCreditModal'
const moment = require('moment')
const columns = [
    {
        title: '客户邮箱',
        dataIndex: 'email',
    },
    {
        title: '客户名',
        dataIndex: 'userName',
    },
    {
        title: '生日',
        dataIndex: 'birthday',
        scopedSlots: { customRender: 'birthday' },
    },
    {
        title: '信用值',
        dataIndex: 'credit',
    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' },
    },
];
export default {
    name: "manageClient",
    data(){
        return {
            columns,
            form: this.$form.createForm(this, { name: 'manageClient' }),
        }
    },
    components: {
        RechargeCreditModal
    },
    computed: {
        ...mapGetters([
            'clientList',
            'userInfo',
            'activeClientId',
            'rechargeCreditModalVisible',
        ]),
    },
    async mounted() {
        await this.getClientList()
        await this.getUserInfo()
    },
    methods: {
        ...mapMutations([
            'set_rechargeCreditModalVisible',
            'set_activeClientId',
        ]),
        ...mapActions([
            'getClientList',
            'getUserInfo'
        ]),
        moment,
        rechargeCredit(record){
            this.set_activeClientId(record.id)
            this.set_rechargeCreditModalVisible(true)
        }
    }
}
</script>

<style scoped lang="less">
    .manageClient-wrapper {
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
    .manageClient-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">

</style>