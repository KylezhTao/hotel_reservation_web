<template>
    <a-modal
            :visible="updateOrderInfoModalVisible"
            title="更新订单信息"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="变更状态">
                <a-radio-group @change="onChange"
                        v-decorator="[
                        'state',
                        { rules: [{required: true, message: '请选择变更状态', }] }
                    ]"
                >
                    <a-radio value="已执行" :disabled="userInfo.userType!=='HotelManager'">已执行</a-radio>
                    <a-radio value="异常" :disabled="userInfo.userType!=='HotelManager'">异常</a-radio>
                    <a-radio value="已撤销" :disabled="userInfo.userType==='HotelManager'">已撤销</a-radio>
                </a-radio-group>
            </a-form-item>
            <a-form-item label="恢复信用值"
                         v-if="userInfo.userType!=='HotelManager'"
            >
                <a-radio-group

                        v-decorator="[
                            'isHalf',
                        { rules: [{required: true, message: '请选择恢复信用值', }] }
                        ]">
                    <a-radio :value=1>总价的一半</a-radio>
                    <a-radio :value=0>总价的全部</a-radio>
                </a-radio-group>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: "updateOrderInfoModal",
        data(){
            return{
                formItemLayout: {
                    labelCol: {
                        xs: { span: 12 },
                        sm: { span: 6 },
                    },
                    wrapperCol: {
                        xs: { span: 24 },
                        sm: { span: 16 },
                    },
                },
            }
        },
        computed: {
            ...mapGetters([
                'userInfo',
                'activeOrderId',
                'updateOrderInfoModalVisible',
            ])
        },
        beforeCreate() {
            // 表单名默认为“form”
            this.form = this.$form.createForm(this, {name: 'updateOrderInfoModal'});
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_updateOrderInfoModalVisible',

            ]),
            ...mapActions([
                'updateOrderInfo'
            ]),
            cancel() {
                this.set_updateOrderInfoModalVisible(false)
            },
            onChange(e) {

            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            id: this.activeOrderId,
                            orderState: this.form.getFieldValue('state'),
                            isHalf: this.form.getFieldValue('isHalf'),
                        }
                        this.updateOrderInfo(data)
                    }
                });
            },
        }
    }
</script>

<style scoped>

</style>