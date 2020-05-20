<template>
    <a-modal
            :visible="updateHotelInfoModalVisible"
            title="维护酒店信息"
            cancelText="取消"
            okText="确定"
            @cancel="cancel"
            @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="商圈">
                <a-select
                        v-decorator="['BizRegion', { rules: [{ required: true, message: '请选择商圈' }] }]"
                >
                    <a-select-option value="Xidan">西单</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="地址">
                <a-input
                        placeholder="请填写酒店地址"
                        v-decorator="['address', { rules: [{ required: true, message: '请填写酒店地址' }] }]"
                />
            </a-form-item>
            <a-form-item label="简介" v-bind="formItemLayout">
                <a-input
                        type="textarea"
                        :rows="4"
                        placeholder="请填写简介"
                        v-decorator="['description', { rules: [{ required: true, message: '请填写简介' }] }]"
                />
            </a-form-item>
            <a-form-item label="设施服务" v-bind="formItemLayout">
                <a-select
                        mode="multiple"
                        style="width: 100%"
                        v-decorator="['service', { rules: [{ required: true, message: '请选择设施服务' }] }]"
                >
                    <a-select-option value="Wi-Fi">Wi-Fi</a-select-option>
                    <a-select-option value="热水">热水</a-select-option>
                    <a-select-option value="叫醒服务">叫醒服务</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="酒店星级" v-bind="formItemLayout">
                <a-select
                        v-decorator="[
                    'hotelStar',
                    { rules: [{ required: true, message: '请选择酒店星级' }] }]"
                        @change="changeStar"
                >
                    <a-select-option value="Three">三星级</a-select-option>
                    <a-select-option value="Four">四星级</a-select-option>
                    <a-select-option value="Five">五星级</a-select-option>
                </a-select>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
export default {
    name: "updateHotelInfoModal",
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
            'activeHotelId',
            'updateHotelInfoModalVisible',
        ])
    },
    beforeCreate() {
        // 表单名默认为“form”
        this.form = this.$form.createForm(this, {name: 'updateHotelInfoModal'});
    },
    mounted() {

    },
    methods: {
        ...mapMutations([
            'set_updateHotelInfoModalVisible',
        ]),
        ...mapActions([
            'updateHotelInfo'
        ]),
        cancel() {
            this.set_updateHotelInfoModalVisible(false)
        },
        changeType(v){

        },
        changeStar(v){

        },
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    const data = {
                        address: this.form.getFieldValue('address'),
                        bizRegion: this.form.getFieldValue('BizRegion'),
                        hotelStar: this.form.getFieldValue('hotelStar'),
                        hotelService: this.form.getFieldValue('service').join(', '),
                        description: this.form.getFieldValue('description'),
                    }
                    this.updateHotelInfo(data)
                }
            });
        },
    },
}
</script>

<style scoped>

</style>