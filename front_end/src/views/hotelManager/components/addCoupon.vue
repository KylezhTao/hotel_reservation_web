<template>
    <a-modal
        :visible="addCouponVisible"
        title="添加优惠策略"
        cancelText="取消"
        okText="确定"
        @cancel="cancel"
        @ok="handleSubmit"
    >
        <a-form :form="form" style="margin-top: 30px" v-bind="formItemLayout">
            <a-form-item label="优惠券类型" v-bind="formItemLayout">
                <a-select
                        v-decorator="['couponType', { rules: [{ required: true, message: '请选择优惠券类型' }] }]"
                        @change="changeType"
                >
                    <a-select-option value="1">生日特惠</a-select-option>
                    <a-select-option value="2">多间特惠</a-select-option>
                    <a-select-option value="3">满减优惠</a-select-option>
                    <a-select-option value="4">限时优惠</a-select-option>
                </a-select>
            </a-form-item>
            <a-form-item label="券名" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写券名"
                        v-decorator="['name', { rules: [{ required: true, message: '请填写券名' }] }]"
                />
            </a-form-item>
            <a-form-item label="优惠简介" v-bind="formItemLayout">
                <a-input
                        type="textarea"
                        :rows="4"
                        placeholder="请填写优惠简介"
                        v-decorator="['description', { rules: [{ required: true, message: '请填写优惠简介' }] }]"
                />
            </a-form-item>

            <a-form-item v-if="curCouponType === 2" label="目标房间数量" v-bind="formItemLayout">
                <a-input
                        placeholder="目标房间数量"
                        v-decorator="['targetRoom', { rules: [{ required: true, message: '请填写目标房间数量' }] }]"
                />
            </a-form-item>
            <a-form-item v-if="curCouponType === 3 || curCouponType === 1" label="达标金额" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写达标金额"
                        v-decorator="['targetMoney', { rules: [{ required: true, message: '请填写达标金额' }] }]"
                />
            </a-form-item>
            <a-form-item v-if='curCouponType === 4' v-bind="formItemLayout" label="生效时间">
                <a-range-picker
                        format="YYYY-MM-DD HH:mm:ss"
                        v-decorator="[
                        'date',
                        {
                            rules: [{ required: true, message: '请选择生效时间' }]
                        }
                    ]"
                        :placeholder="['开始时间','结束时间']"
                />
            </a-form-item>

            <a-form-item v-if='curCouponType !== 2' label="优惠金额" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写优惠金额"
                        v-decorator="['discountMoney', { rules: [{ required: true, message: '请填写优惠金额' }] }]"
                />
            </a-form-item>
            <a-form-item v-if='curCouponType === 2' label="优惠折扣" v-bind="formItemLayout">
                <a-input
                        placeholder="请填写优惠折扣"
                        v-decorator="['discount', { rules: [{ required: true, message: '请填写优惠折扣' }] }]"
                />
            </a-form-item>
        </a-form>

    </a-modal>
</template>
<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
const moment = require('moment')
export default {
    name: 'addCouponModal',
    data() {
        return {
            curCouponType: 0,
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
            'activeHotelId',
            'addCouponVisible',
        ])
    },
    beforeCreate() {
        // 表单名默认为“form”
        this.form = this.$form.createForm(this, { name: 'addCouponModal' });
    },
    mounted() {

    },
    methods: {
        ...mapMutations([
            'set_addCouponVisible'
        ]),
        ...mapActions([
            'addHotelTargetMoneyCoupon',
            'addHotelTimeCoupon',
            'addHotelTargetRoomCoupon'
        ]),
        cancel() {
            this.set_addCouponVisible(false)
        },
        changeType(v){
            this.curCouponType = Number(v)
        },
        changeDate(v) {

        },
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    if(this.curCouponType === 3 || this.curCouponType === 1){ // 满减或生日
                        const data = {
                            // 这里添加接口参数，这里的参数名是相应VO里的变量名
                            name: this.form.getFieldValue('name'),
                            description: this.form.getFieldValue('description'),
                            type: Number(this.form.getFieldValue('couponType')),
                            targetMoney: Number(this.form.getFieldValue('targetMoney')),
                            discountMoney: Number(this.form.getFieldValue('discountMoney')),
                            hotelId: this.userInfo.userType === 'Marketer'?-1:Number(this.activeHotelId),
                            status: 1
                        }
                        this.addHotelTargetMoneyCoupon(data)
                    }else if(this.curCouponType === 4){ // 限时
                        const data = {
                            // 这里添加接口参数，这里的参数名是相应VO里的变量名
                            name: this.form.getFieldValue('name'),
                            description: this.form.getFieldValue('description'),
                            type: Number(this.form.getFieldValue('couponType')),
                            startTime: moment(this.form.getFieldValue('date')[0]),
                            endTime: moment(this.form.getFieldValue('date')[1]),
                            hotelId: this.userInfo.userType === 'Marketer'?-1:Number(this.activeHotelId),
                            discountMoney: Number(this.form.getFieldValue('discountMoney')),
                            status: 1
                        }
                        this.addHotelTimeCoupon(data)
                    }else if(this.curCouponType === 2){ // 多间
                        const data = {
                            // 这里添加接口参数
                            name: this.form.getFieldValue('name'),
                            description: this.form.getFieldValue('description'),
                            type: Number(this.form.getFieldValue('couponType')),
                            targetRoom: Number(this.form.getFieldValue('targetRoom')),
                            discount: Number(this.form.getFieldValue('discount')),
                            hotelId: this.userInfo.userType === 'Marketer'?-1:Number(this.activeHotelId),
                            status: 1
                        }
                        this.addHotelTargetRoomCoupon(data)
                    }
                }
            });
        },
    }
}
</script>