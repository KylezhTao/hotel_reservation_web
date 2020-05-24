const getters = {
  //user
  token: state => state.user.token,
  userId: state => state.user.userId,
  userInfo: state => state.user.userInfo,
  userOrderList: state => state.user.userOrderList,
  userCreditRecordList: state => state.user.userCreditRecordList,
  userHotelOrderList: state => state.user.userHotelOrderList,
  managerOrderList: state => state.user.managerOrderList,
  hotelListLoading: state => state.hotel.hotelListLoading,
  hotelList: state => state.hotel.hotelList,
  currentHotelInfo: state => state.hotel.currentHotelInfo,
  currentHotelId: state => state.hotel.currentHotelId,
  orderModalVisible: state => state.hotel.orderModalVisible,
  currentOrderRoom: state => state.hotel.currentOrderRoom,
  orderMatchCouponList: state => state.hotel.orderMatchCouponList,
  commentsOfHotel: state => state.hotel.commentsOfHotel,
  //admin
  userList: state => state.admin.userList,
  managerList: state => state.admin.managerList,
  addManagerModalVisible: state => state.admin.addManagerModalVisible,
  addManagerParams: state => state.admin.addManagerParams,
  addMarketerModalVisible: state => state.admin.addMarketerModalVisible,
  addMarketerParams: state => state.admin.addMarketerParams,
  //hotelManager
  orderList: state => state.hotelManager.orderList,
  activeOrderId: state => state.hotelManager.activeOrderId,
  addHotelModalVisible: state => state.hotelManager.addHotelModalVisible,
  addRoomModalVisible: state => state.hotelManager.addRoomModalVisible,
  updateHotelInfoModalVisible: state => state.hotelManager.updateHotelInfoModalVisible,
  updateOrderInfoModalVisible: state => state.hotelManager.updateOrderInfoModalVisible,
  couponVisible: state => state.hotelManager.couponVisible,
  addCouponVisible: state => state.hotelManager.addCouponVisible,
  activeHotelId: state => state.hotelManager.activeHotelId,
  couponList: state => state.hotelManager.couponList,
  //Marketer:
  clientList: state => state.marketer.clientList,
  rechargeCreditModalVisible: state => state.marketer.rechargeCreditModalVisible,
  activeClientId: state => state.marketer.activeClientId,

  }
  
  export default getters