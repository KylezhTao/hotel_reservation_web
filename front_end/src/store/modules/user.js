import router from '@/router'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'
import { message } from 'ant-design-vue'
import {
    loginAPI,
    registerAPI,
    getUserInfoAPI,
    updateUserInfoAPI,
    makeCommentAPI,
    getUserCreditRecordsAPI
} from '@/api/user'

import {
    getUserOrdersAPI,
    cancelOrderAPI,
    getManagerOrdersAPI,
    getUserHotelOrdersAPI
} from '@/api/order'

const getDefaultState = () => {
    return {
        userId: '',
        userInfo: {

        },
        activeUserId: 0,
        userOrderList: [

        ],
        managerOrderList: [

        ],
        userHotelOrderList: [

        ],
        userCreditRecordList: [

        ],
    }
}

const user = {
    state : getDefaultState(),

    mutations: {
        reset_state: function(state) {
            state.token = '',
            state.userId = '',
            state.userInfo = {
                
            },
            state.userOrderList = []
        },
        set_token: function(state, token){
            state.token = token
        },
        set_email: (state, data) => {
            state.email = data
        },
        set_userId: (state, data) => {
            state.userId = data
        },
        set_userInfo: (state, data) => {
            state.userInfo = {
                ...state.userInfo,
                ...data
            }
        },
        set_activeUserId: function(state, data) {
            state.activeUserId = data
        },
        set_userOrderList: (state, data) => {
            state.userOrderList = data
        },
        set_managerOrderList: function(state, data) {
            state.managerOrderList = data
        },
        set_userHotelOrderList: function(state, data) {
            state.userHotelOrderList = data
        },
        set_userCreditRecordList: function (state, data) {
            state.userCreditRecordList = data
        }
    },

    actions: {
        login: async ({ dispatch, commit }, userData) => {
            const res = await loginAPI(userData)
            if(res){
                setToken(res.id)
                commit('set_userId', res.id)
                dispatch('getUserInfo')
                router.push('/hotel/hotelList')
            }
        },
        register: async({ commit }, data) => {
            const res = await registerAPI(data)
            message.success('注册成功')
        },
        getUserInfo({ state, commit }) {
            return new Promise((resolve, reject) => {
              getUserInfoAPI(state.userId).then(response => {
                const data = response
                if (!data) {
                  reject('登录已过期，请重新登录')
                }
                commit('set_userInfo', data)
                commit('set_userId', data.id)
                resolve(data)
              }).catch(error => {
                reject(error)
              })
            })
        },
        updateUserInfo: async({ state, dispatch }, data) => {
            const params = {
                id: state.userId,
                ...data,
            }
            const res = await updateUserInfoAPI(params)
            if(res){
                dispatch('getUserInfo')
                message.success('修改成功')
            }
        },
        makeComment: async({ state, dispatch }, data) => {
            const res = await makeCommentAPI(data)
            if(res){
                dispatch('getCommentsByHotelId')
                message.success('评论成功')
            }else{
                message.error('评论失败')
            }
        },
        getUserOrders: async({ state, commit }) => {
            const data = {
                userId: Number(state.userId)
            }
            const res = await getUserOrdersAPI(data)
            if(res){
                commit('set_userOrderList', res)
            }
        },
        getUserCreditRecords: async({ state, commit }) => {
            const data = {
                clientId: Number(state.userId)
            }
            const res = await getUserCreditRecordsAPI(data)
            if(res){
                commit('set_userCreditRecordList', res)
            }
        },
        getManagerOrders: async({ state, commit }) => { // my -> manager
            const data = {
                userId: Number(state.userId)
            }
            const res = await getManagerOrdersAPI(data)
            if(res){
                commit('set_managerOrderList', res)
            }
        },
        getUserHotelOrders: async({ state, commit }, hotelId) => {
            const params = {
                userId: Number(state.userId),
                hotelId: hotelId,
            }
            const res = await getUserHotelOrdersAPI(params)
            if(res){
                commit('set_userHotelOrderList', res)
            }
        },
        cancelOrder: async({ state, dispatch }, orderId) => {
            const res = await cancelOrderAPI(orderId)
            if(res) {
                dispatch('getUserOrders')
                message.success('撤销成功')
            }else{
                message.error('撤销失败')
            }
        },
        logout: async({ commit }) => {
            removeToken()
            resetRouter()
            commit('reset_state')
        },
          // remove token
        resetToken({ commit }) {
            return new Promise(resolve => {
                removeToken() // must remove  token  first
                commit('reset_state')
                resolve()
            })
        },
    }
}

export default user