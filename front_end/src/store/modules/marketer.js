import {
    getClientListAPI,
    rechargeCreditAPI
} from '@/api/marketer'
import { message } from 'ant-design-vue'
const marketer = {
    state: {
        clientList: [

        ],
        rechargeCreditModalVisible: false,
        activeClientId: 0,

    },

    mutations: {
        set_clientList: function(state, data) {
            state.clientList = data
        },
        set_rechargeCreditModalVisible: function (state, data) {
            state.rechargeCreditModalVisible = data
        },
        set_activeClientId: function (state, data) {
            state.activeClientId = data
        }
    },

    actions: {
        getClientList: async({ commit }) => {
            const res = await getClientListAPI()
            if(res){
                commit('set_clientList', res)
            }
        },
        rechargeCredit: async({ commit, dispatch }, data) => {
            const res = await rechargeCreditAPI(data)
            if(res){
                commit('set_rechargeCreditModalVisible', false)
                message.success('充值成功')
            }else{
                message.error('充值失败')
            }
        },
    },

}
export default marketer