<template>
    <div class="room-list">
        <div class="filter">

        </div>
        <div class="list">
            <a-table
                :columns="columns"
                :dataSource="rooms"
            >
                <span slot="price" slot-scope="text">
                    <span>￥{{ text }}</span>
                </span>
                <span slot="roomType" slot-scope="text">
                    <a-tag>{{text}}</a-tag>
                </span>
                <span slot="action" slot-scope="text, record">
                    <a-button type="primary" @click="order(record)">预订</a-button>
                </span>
            </a-table>
        </div>
        <OrderModal></OrderModal>
    </div>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import OrderModal from './orderModal'
const columns = [
    {  
      title: '房型',
      dataIndex: 'roomType',
      key: 'roomType',
      scopedSlots: { customRender: 'roomType'}
    },
    {
        title: '现有数量',
        dataIndex: 'curNum',
        key: 'curNum',
    },
    {
      title: '早餐',
      dataIndex: 'breakfast',
      key: 'breakfast',
    },
    {
      title: '房价',
      key: 'price',
      dataIndex: 'price',
      scopedSlots: { customRender: 'price'}
    },
    {
      title: '操作',
      key: 'action',
      scopedSlots: { customRender: 'action' },
    },
  ];
export default {
    name:'roomList',
    props: {
        rooms: {
            type: Array
        }
    },
    data() {
        return {
            columns,
        }
    },
    components: {
        OrderModal
    },
    computed: {
        ...mapGetters([
            'orderModalVisible'
        ])
    },
    monuted() {

    },
    methods: {
        ...mapMutations([
            'set_orderModalVisible',
            'set_currentOrderRoom'
        ]),
        ...mapActions([

        ]),
        order(record) {
            this.set_currentOrderRoom(record)
            this.set_orderModalVisible(true)
        }
    }
}
</script>