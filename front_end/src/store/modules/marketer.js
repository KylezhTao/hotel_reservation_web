import {
    getClientListAPI,
} from '@/api/marketer'
import { message } from 'ant-design-vue'
const marketer = {
    state: {
        clientList: [

        ],

    },

    mutations: {
        set_clientList: function(state, data) {
            state.clientList = data
        },
    },

    actions: {
        getClientList: async({ commit }) => {
            const res = await getClientListAPI()
            if(res){
                commit('set_clientList', res)
            }
        },
    },

}
export default marketer