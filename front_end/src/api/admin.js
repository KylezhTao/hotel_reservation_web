import { axios } from '@/utils/request'
const api = {
    adminPre: '/api/admin'
}
export function getManagerListAPI(){
    return axios({
        url: `${api.adminPre}/getAllManagers`,
        method: 'POST'
    })
}
export function getUserListAPI(){
    return axios({
        url: `${api.adminPre}/getAllUsers`,
        method: 'POST'
    })
}
export function addManagerAPI(data) {
    return axios({
        url: `${api.adminPre}/addManager`,
        method: 'POST',
        data
    })
}
export function addMarketerAPI(data) {
    return axios({
        url: `${api.adminPre}/addMarketer`,
        method: 'POST',
        data
    })
}