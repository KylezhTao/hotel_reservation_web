<template>
    <a-modal
            :visible="rechargeCreditModalVisible"
            title="信用充值"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="充值金额" v-bind="formItemLayout">
                <a-input prefix="￥" suffix="RMB"
                        placeholder=" 请填写充值金额 （单位：元）"
                        v-decorator="['amount', { rules: [{ required: true, message: '请填写充值金额 （单位：元）' }] }]"
                />
            </a-form-item>
            <a-form-item label="将充值" v-bind="formItemLayout">
                <span>{{ Number(this.form.getFieldValue('amount')) * 100 }} 点信用值</span>
            </a-form-item>
        </a-form>

    </a-modal>
</template>

<script>
    import { mapGetters, mapMutations, mapActions } from 'vuex'
    export default {
        name: "rechargeCreditModal",
        data() {
            return {
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
                'rechargeCreditModalVisible',
                'activeClientId'
            ])
        },
        beforeCreate() {
            this.form = this.$form.createForm(this, { name: 'rechargeCreditModal' });
        },
        mounted() {

        },
        methods: {
            ...mapMutations([
                'set_rechargeCreditModalVisible',
            ]),
            ...mapActions([
                'rechargeCredit',
            ]),
            cancel() {
                this.set_rechargeCreditModalVisible(false)
            },
            handleSubmit(e) {
                e.preventDefault();
                this.form.validateFieldsAndScroll((err, values) => {
                    if (!err) {
                        const data = {
                            id: this.activeClientId,
                            credit: Number(this.form.getFieldValue('amount')) * 100,
                        }
                        this.rechargeCredit(data)
                    }
                });
            },
        }
    }

</script>

<style scoped>

</style>