import {
    addRoomAPI,
    addHotelAPI,
} from '@/api/hotelManager'
import {
    getAllOrdersAPI,
    updateOrderInfoAPI
} from '@/api/order'
import {
    hotelAllCouponsAPI,
    hotelTargetMoneyAPI,
    hotelTimeAPI,
    hotelTargetRoomAPI,
} from '@/api/coupon'
import {
    updateHotelInfoAPI,
} from '@/api/hotel'
import { message } from 'ant-design-vue'

const hotelManager = {
    state: {
        orderList: [],
        addHotelParams: {
            name: '',
            address: '',
            bizRegion:'XiDan',
            hotelStar:'',
            hotelService:'',
            rate: 0,
            description:'',
            phoneNum:'',
            managerId:'',
        },
        addHotelModalVisible: false,
        addRoomParams: {
            roomType: '',
            hotelId: '',
            price: '',
            total: 0,
            curNum: 0,
        },
        addRoomModalVisible: false,
        updateHotelInfoModalVisible: false,
        updateOrderInfoModalVisible: false,
        couponVisible: false,
        addCouponVisible: false,
        activeHotelId: 0,
        activeOrderId: 0,
        couponList: [],
    },
    mutations: {
        set_orderList: function(state, data) {
            state.orderList = data
        },
        set_updateHotelInfoModalVisible: function(state, data) {
            state.updateHotelInfoModalVisible = data
        },
        set_updateOrderInfoModalVisible: function(state, data) {
            state.updateOrderInfoModalVisible = data
        },
        set_addHotelModalVisible: function(state, data) {
            state.addHotelModalVisible = data
        },
        set_addHotelParams: function(state, data) {
            state.addHotelParams = {
                ...state.addHotelParams,
                ...data,
            }
        },
        set_addRoomModalVisible: function(state, data) {
            state.addRoomModalVisible = data
        },
        set_addRoomParams: function(state, data) {
            state.addRoomParams = {
                ...state.addRoomParams,
                ...data,
            }
        },
        set_couponVisible: function(state, data) {
            state.couponVisible = data
        },
        set_activeHotelId: function(state, data) {
            state.activeHotelId = data
        },
        set_activeOrderId: function(state, data){
            state.activeOrderId = data
        },
        set_couponList: function(state, data) {
            state.couponList = data
        },
        set_addCouponVisible: function(state, data) {
            state.addCouponVisible = data
        }
    },
    actions: {
        getAllOrders: async({ state, commit }) => {
            const res = await getAllOrdersAPI()
            if(res){
                commit('set_orderList', res)
            }
        },

        addHotel: async({ state, dispatch, commit }) => {
            const res = await addHotelAPI(state.addHotelParams)
            if(res){
                dispatch('getHotelList')
                commit('set_addHotelParams', {
                    name: '',
                    address: '',
                    bizRegion:'XiDan',
                    hotelStar:'',
                    hotelService:'',
                    rate: 0,
                    description:'',
                    phoneNum:'',
                    managerId:'',
                })
                commit('set_addHotelModalVisible', false)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        addRoom: async({ state, dispatch, commit }) => {
            const res = await addRoomAPI(state.addRoomParams)
            if(res){
                commit('set_addRoomParams', {
                    roomType: '',
                    hotelId: '',
                    price: '',
                    total: 0,
                    curNum: 0,
                })
                commit('set_addRoomModalVisible', false)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        getHotelCoupon: async({ state, commit }) => {
            const res = await hotelAllCouponsAPI(state.activeHotelId)
            if(res) {
                // 获取到酒店策略之后的操作（将获取到的数组赋值给couponList）
                commit('set_couponList', res)
            }
        },
        addHotelTargetMoneyCoupon: async({ commit, dispatch }, data) => {
            const res = await hotelTargetMoneyAPI(data)
            if(res){
                // 添加成功后的操作（提示文案、modal框显示与关闭，调用优惠列表策略等）
                dispatch('getHotelCoupon')
                commit('set_addCouponVisible', false)
                commit('set_couponVisible', true)
                message.success('添加成功')
            }else{
                // 添加失败后的操作
                message.error('添加失败')
            }
        },
        addHotelTimeCoupon: async({ commit, dispatch }, data) => {
            const res = await hotelTimeAPI(data)
            if(res){
                dispatch('getHotelCoupon')
                commit('set_addCouponVisible', false)
                commit('set_couponVisible', true)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        addHotelTargetRoomCoupon: async({ commit, dispatch }, data) => {
            const res = await hotelTargetRoomAPI(data)
            if(res){
                dispatch('getHotelCoupon')
                commit('set_addCouponVisible', false)
                commit('set_couponVisible', true)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        },
        updateHotelInfo: async({ commit, state, dispatch }, data) => {
            const params = {
                hotelId: state.activeHotelId,
                ...data,
            }
            const res = await updateHotelInfoAPI(params)
            if(res){
                dispatch('getHotelById')
                commit('set_updateHotelInfoModalVisible',false)
                message.success('更新成功')
            }
        },
        updateOrderInfo: async({ commit, state, dispatch}, data) => {
            const res = await updateOrderInfoAPI(data)
            if(res){
                dispatch('getManagerOrders')
                dispatch('getAllOrders')
                commit('set_updateOrderInfoModalVisible',false)
                message.success('更新成功')
            }
        },
    }
}

export default hotelManager