<template>
    <div class="manageHotel-wrapper">
        <a-tabs>
            <a-tab-pane tab="酒店管理" key="1">
                <div style="width: 100%; text-align: right; margin:20px 0">
                    <a-button v-if="userInfo.userType==='HotelManager'"
                              @click="myHotelFilter"><a-icon type="filter"/>我管理的酒店</a-button>
                    <a-divider type="vertical"></a-divider>
                    <a-button v-if="userInfo.userType==='HotelManager'"
                              @click="reset"><a-icon type="redo"/>所有酒店</a-button>
                    <a-divider type="vertical"></a-divider>
                    <a-button v-if="userInfo.userType==='HotelManager'" type="primary"
                              @click="addHotel"><a-icon type="plus" />添加酒店</a-button>
                </div>
                 <a-table
                    :columns="columns1"
                    :dataSource="isFilter?res:hotelList"
                    bordered
                >
                     <a-tag :color="text==='Five'?'orange':text==='Four'?'blue':'green'"
                            slot="hotelStar" slot-scope="text">
                         {{text}}
                     </a-tag>
                     <a slot="hotelName" slot-scope="text">
                         {{ text }}
                     </a>
                     <span slot="rate" slot-scope="text" >
                        <a-statistic :value="text"
                                     :precision="2"
                        >
                                    <template #suffix>
                                        <span> / 5</span>
                                    </template>
                        </a-statistic>
                    </span>

                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small" icon="form"
                                  :disabled="record.managerId !== Number(userId)"
                                  @click="maintainInfo(record)">维护信息</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-button type="primary" size="small" icon="plus"
                                  :disabled="record.managerId !== Number(userId)"
                                  @click="addRoom(record)">录入房间</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-button size="small" icon="bars"
                                  :disabled="record.managerId !== Number(userId) && userInfo.userType!=='Marketer'"
                                  @click="showCoupon(record)">优惠策略</a-button>
                        <a-divider type="vertical"></a-divider>
                        <a-popconfirm
                            title="确定想删除该酒店吗？"
                            @confirm="deleteHotel(record)"
                            okText="确定"
                            cancelText="取消"
                        >
                            <a-button type="danger" size="small" icon="delete"
                                      :disabled="userInfo.userType!=='Admin'">
                                删除酒店
                            </a-button>
                        </a-popconfirm>
                    </span>
                </a-table>
            </a-tab-pane>
            <a-tab-pane v-if="userInfo.userType!=='Admin'" tab="订单管理" key="2">
                <a-table
                    :columns="columns2"
                    :dataSource="userInfo.userType==='Marketer'?orderList:managerOrderList"
                    class="components-table-demo-nested"
                    bordered
                >
                    <span slot="expandedRowRender" slot-scope="record" style="margin: 0">
                        <a-tag>- 生成时间： {{ record.createDate }}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 入住日期： {{ record.checkInDate }}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 退房日期： {{record.checkOutDate}}</a-tag>
                        <a-divider type="vertical"></a-divider>
                        <a-tag>- 订单最晚执行时间： {{record.latestExecTime}}</a-tag>
                    </span>
                    <span slot="price" slot-scope="text">
                        <span>￥{{ text }}</span>
                    </span>
                    <span slot="roomType" slot-scope="text">
                        <a-tag v-if="text === 'BigBed'">大床房</a-tag>
                        <a-tag v-if="text === 'DoubleBed'">双床房</a-tag>
                        <a-tag v-if="text === 'Family'">家庭房</a-tag>
                    </span>
                    <span slot="haveChild" slot-scope="text">
                        <span v-if="text === true">有</span>
                        <span v-if="text === false">无</span>
                    </span>
                    <a-tag slot="orderState" :color="text==='已预订'?'#108ee9':text==='已执行'?'#87d068':text==='已撤销'?'#e8e819':'#ff0000'" slot-scope="text">
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">
                        <a-button type="primary" size="small" icon="tool"
                                  v-if="(userInfo.userType==='HotelManager' && record.orderState==='已预订' || record.orderState==='异常') ||
                                        (userInfo.userType==='Marketer' && record.orderState==='异常')"
                                  @click="updateOrder(record)">变更订单状态</a-button>
                    </span>
                </a-table>
            </a-tab-pane>
        </a-tabs>
        <UpdateHotelInfoModal></UpdateHotelInfoModal>
        <AddHotelModal></AddHotelModal>
        <AddRoomModal></AddRoomModal>
        <Coupon></Coupon>
        <UpdateOrderInfoModal></UpdateOrderInfoModal>
    </div>
</template>

<script>
import { mapGetters, mapMutations, mapActions } from 'vuex'
import AddHotelModal from './components/addHotelModal'
import AddRoomModal from './components/addRoomModal'
import Coupon from './components/coupon'
import UpdateHotelInfoModal from './components/updateHotelInfoModal'
import UpdateOrderInfoModal from './components/updateOrderInfoModal'
const moment = require('moment')
const columns1 = [
    {  
        title: '酒店名',
        dataIndex: 'name',
    },
    {
        title: '商圈',
        dataIndex: 'bizRegion',
    },
    {
        title: '地址',
        dataIndex: 'address',
    },
    {
        title: '酒店星级',
        dataIndex: 'hotelStar',
        scopedSlots:{customRender: 'hotelStar'},
    },
    {
        title: '评分',
        dataIndex: 'rate',
        scopedSlots:{customRender: 'rate'},
    },
    {
        title: '简介',
        dataIndex: 'description',
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
const columns2 = [
    {  
        title: '订单号',
        dataIndex: 'id',
    },
    {  
        title: '酒店名',
        dataIndex: 'hotelName',
        scopedSlots: { customRender: 'hotelName' },
    },
    {
        title: '房型',
        dataIndex: 'roomType',
        scopedSlots: { customRender: 'roomType' }
    },
    {
        title: '房间数量',
        dataIndex: 'roomNum',
    },
    {
        title: '预计入住人数',
        dataIndex: 'peopleNum',
    },
    {
        title: '有无儿童',
        dataIndex: 'haveChild',
        scopedSlots: { customRender: 'haveChild' }
    },
    {
        title: '房价',
        dataIndex: 'price',
    },
    {
        title: '状态',
        filters: [{ text: '已预订', value: '已预订' }, { text: '已撤销', value: '已撤销' }, { text: '已执行', value: '已执行' }],
        onFilter: (value, record) => record.orderState.includes(value),
        dataIndex: 'orderState',
        scopedSlots: { customRender: 'orderState' }
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
export default {
    name: 'manageHotel',
    data(){
        return {
            formLayout: 'horizontal',
            isFilter: false,
            res: [],
            pagination: {},
            columns1,
            columns2,
            form: this.$form.createForm(this, { name: 'manageHotel' }),
        }
    },
    components: {
        AddHotelModal,
        AddRoomModal,
        UpdateHotelInfoModal,
        Coupon,
        UpdateOrderInfoModal,
    },
    computed: {
        ...mapGetters([
            'userId',
            'userInfo',
            'orderList',
            'managerOrderList',
            'hotelList',
            'addHotelModalVisible',
            'addRoomModalVisible',
            'updateHotelInfoModalVisible',
            'updateOrderInfoModalVisible',
            'activeHotelId',
            'couponVisible',
        ]),
    },
    async mounted() {
        await this.getUserInfo()
        await this.getHotelList()
        await this.getAllOrders()
        await this.getManagerOrders()
    },
    methods: {
        ...mapMutations([
            'set_updateHotelInfoModalVisible',
            'set_updateOrderInfoModalVisible',
            'set_addHotelModalVisible',
            'set_addRoomModalVisible',
            'set_couponVisible',
            'set_activeHotelId',
            'set_activeOrderId'
        ]),
        ...mapActions([
            'getUserInfo',
            'getHotelList',
            'getAllOrders',
            'getManagerOrders',
            'getHotelCoupon'
        ]),
        addHotel() {
            this.set_addHotelModalVisible(true)
        },
        addRoom(record) {
            this.set_activeHotelId(record.id)
            this.set_addRoomModalVisible(true)
        },
        maintainInfo(record){
            this.set_activeHotelId(record.id)
            this.set_updateHotelInfoModalVisible(true)
        },
        showCoupon(record) {
            this.set_activeHotelId(record.id)
            this.set_couponVisible(true)
            this.getHotelCoupon()
        },
        myHotelFilter(){
            this.res = this.hotelList.filter(item => item.managerId === Number(this.userId))
            this.isFilter = true
        },
        reset(){
            this.isFilter = false
        },
        deleteHotel(record){

        },
        updateOrder(record){
            this.set_activeOrderId(record.id)
            this.set_updateOrderInfoModalVisible(true)
        },
    }
}
</script>
<style scoped lang="less">
    .manageHotel-wrapper {
        padding: 50px;
        .chart {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-top: 20px
        }
    }
</style>
<style lang="less">
    .manageHotel-wrapper {
        .ant-tabs-bar {
            padding-left: 30px
        }
    }
</style>
<style lang="less">
    
</style>