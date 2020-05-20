<template>
    <div class="manageClient-wrapper">
        <a-tabs>
            <a-tab-pane tab="信用充值" key="1">
                <a-table
                        :columns="columns"
                        :dataSource="clientList"
                        bordered
                >
                </a-table>
            </a-tab-pane>
        </a-tabs>
    </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
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

    },
    computed: {
        ...mapGetters([
            'clientList',
            'userInfo'
        ]),
    },
    async mounted() {
        await this.getClientList()
        await this.getUserInfo()
    },
    methods: {
        ...mapActions([
            'getClientList',
            'getUserInfo'
        ]),
        ...mapMutations([

        ]),
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