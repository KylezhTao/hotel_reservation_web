import { axios } from '@/utils/request'
const api = {
    hotelPre: '/api/hotel'
}
export function getHotelsAPI() {
    return axios({
        url: `${api.hotelPre}/all`,
        method: 'GET',
    })
}
export function getHotelByIdAPI(param) {
    return axios({
        url: `${api.hotelPre}/${param.hotelId}/detail`,
        method: 'GET',
    })
}


// 把data改成param会出错，收不到请求主体（request payload）???
// 感觉是触发了某种关键字
export function updateHotelInfoAPI(data) {
    return axios({
        url: `${api.hotelPre}/${data.hotelId}/update`,
        method: 'POST',
        data,
    })
}
