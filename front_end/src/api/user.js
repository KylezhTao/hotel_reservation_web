import { axios } from '@/utils/request'

const api = {
    userPre: '/api/user'
}
export function loginAPI(data){
    return axios({
        url:`${api.userPre}/login`,
        method: 'POST',
        data
    })
}
export function registerAPI(data){
    return axios({
        url: `${api.userPre}/register`,
        method: 'POST',
        data
    })
}
export function getUserInfoAPI(id){
    return axios({
        url: `${api.userPre}/${id}/getUserInfo`,
        method: 'GET'
    })
}
export function getUserCreditRecordsAPI(data){
    return axios({
        url: `${api.userPre}/${data.clientId}/getUserCreditRecords`,
        method: 'GET'
    })
}
export function updateUserInfoAPI(data) {
    return axios({
        url: `${api.userPre}/${data.id}/update`,
        method: 'POST',
        data,
    })
}
export function makeCommentAPI(data) {
    return axios({
        url: `${api.userPre}/${data.authorId}/makeComment`,
        method: 'POST',
        data,
    })
}