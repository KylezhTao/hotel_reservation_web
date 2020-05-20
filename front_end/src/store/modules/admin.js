import {
    getManagerListAPI,
    getUserListAPI,
    addManagerAPI,
    addMarketerAPI
} from '@/api/admin'
import { message } from 'ant-design-vue'

const admin = {
    state: {
        managerList: [

        ],
        userList: [

        ],
        addManagerModalVisible: false,
        addManagerParams: {
            email:'',
            password:''
        },
        addMarketerModalVisible: false,
        addMarketerParams: {
            email:'',
            password:''
        }
    },
    mutations: {
        set_userList: function(state, data) {
            state.userList = data
        },
        set_managerList: function(state, data) {
            state.managerList = data
        },
        set_addManagerModalVisible: function(state, data) {
            state.addManagerModalVisible = data
        },
        set_addManagerParams: function(state, data) {
            state.addManagerParams = {
                ...state.addManagerParams,
                ...data,
            }
        },
        set_addMarketerModalVisible: function(state, data) {
            state.addMarketerModalVisible = data
        },
        set_addMarketerParams: function(state, data) {
            state.addMarketerParams = {
                ...state.addMarketerParams,
                ...data,
            }
        },
    },
    actions: {
        getUserList: async({ commit }) => {
            const res = await getUserListAPI()
            if(res){
                commit('set_userList', res)
            }
        },
        getManagerList: async({ commit }) => {
            const res = await getManagerListAPI()
            if(res){
                commit('set_managerList', res)
            }
        },
        addManager: async({ state, commit, dispatch }) => {
            const res = await addManagerAPI(state.addManagerParams)
            if(res) {
                commit('set_addManagerParams',{
                    email:'',
                    password:''
                })
                commit('set_addManagerModalVisible', false)
                message.success('添加成功')
                dispatch('getManagerList')
            }else{
                message.error('添加失败')
            }
        },
        addMarketer: async({ state, commit, dispatch }) => {
            const res = await addMarketerAPI(state.addMarketerParams)
            if(res) {
                commit('set_addMarketerParams',{
                    email:'',
                    password:''
                })
                commit('set_addMarketerModalVisible', false)
                message.success('添加成功')
            }else{
                message.error('添加失败')
            }
        }
    }
}
export default admin