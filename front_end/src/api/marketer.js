import { axios } from '@/utils/request'
const api = {
    marketerPre: '/api/marketer'
}
export function getClientListAPI(){
    return axios({
        url: `${api.marketerPre}/getAllClients`,
        method: 'POST'
    })
}
export function rechargeCreditAPI(data){
    return axios({
        url: `${api.marketerPre}/${data.id}/rechargeCredit`,
        method: 'POST',
        data
    })
}