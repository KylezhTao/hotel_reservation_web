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