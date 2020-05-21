<template>
    <a-layout>
        <a-layout-content>
            <div class="hotelDetailCard">
                <h1>
                    {{ currentHotelInfo.title }}
                </h1>
                <div class="hotel-info">
                    <a-card style="width: 240px">
                        <img
                            alt="example"
                            src="@/assets/cover.jpeg"
                            slot="cover"
                            referrerPolicy="no-referrer"
                            />
                    </a-card>
                    <div class="info">
                        <div class="items" v-if="currentHotelInfo.name">
                            <span class="label">酒店名称：</span>
                            <span class="value">{{ currentHotelInfo.name }}</span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.address">
                            <span class="label">地址：</span>
                            <span class="value">{{ currentHotelInfo.address }}</span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.rate">
                            <span class="label">评分：</span>
                            <a-statistic :value="currentHotelInfo.rate"
                                         :precision="2"
                            >
                                <template #suffix>
                                    <span> / 5</span>
                                </template>
                            </a-statistic>
                        </div>
                        <div class="items" v-if="currentHotelInfo.hotelStar">
                            <span class="label">星级：</span>
                            <a-rate style="font-size: 15px" :value="currentHotelInfo.rate" disabled allowHalf/>
                        </div>
                        <div class="items" v-if="currentHotelInfo.hotelService">
                            <span class="label">设施服务：</span>
                            <span class="value">
                                <a-tag color="geekblue">
                                    {{ currentHotelInfo.hotelService }}
                                </a-tag>
                            </span>
                        </div>
                        <div class="items" v-if="currentHotelInfo.description">
                            <span class="label">酒店简介：</span>
                            <span class="value">{{ currentHotelInfo.description }}</span>
                        </div>
                    </div>
                </div>
                <a-divider></a-divider>
                <a-tabs>
                    <a-tab-pane tab="房间信息" key="1">
                        <RoomList :rooms="currentHotelInfo.rooms"></RoomList>
                    </a-tab-pane>
                    <a-tab-pane tab="酒店详情" key="2">

                    </a-tab-pane>
                    <a-tab-pane tab="我的订单" key="3">
                        <a-table
                                :columns="columns"
                                :dataSource="userHotelOrderList"
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
                    <a-tag slot="orderState" :color="text==='已预订'?'#108ee9':text==='已入住'?'#87d068':text==='已撤销'?'#f6bb03':'#ff0000'" slot-scope="text">
                        {{ text }}
                    </a-tag>
                    <span slot="action" slot-scope="record">
                        <a-popconfirm
                                title="你确定撤销该笔订单吗？"
                                @confirm="confirmCancelOrder(record.id)"
                                @cancel="cancelCancelOrder"
                                okText="确定"
                                cancelText="取消"
                                v-if="record.orderState === '已预订'"
                        >
                            <a-button type="danger" icon="undo" size="small">撤销</a-button>
                        </a-popconfirm>

                    </span>
                        </a-table>
                    </a-tab-pane>
                    <a-tab-pane tab="评论" key="4">
                        <div class="comment">
                            <a-list
                                    v-if="commentsOfHotel.length"
                                    :data-source="commentsOfHotel"
                                    :header="`${commentsOfHotel.length} 条评论`"
                                    item-layout="horizontal"
                            >
                                <a-list-item slot="renderItem" slot-scope="item">
                                    <a-comment
                                            :author="item.author"
                                            avatar="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                                            :content="item.content"
                                            :datetime="moment(item.datetime,'YYYY-MM-DD HH:mm:ss').fromNow()"
                                    >
                                        <a-statistic style="line-height: 1.0" :value="item.rate" :precision="1">
                                            <template #prefix>
                                                <a-rate style="font-size: 15px" :value="item.rate" disabled allowHalf>
                                                    <a-icon slot="character" type="heart" />
                                                </a-rate>
                                            </template>
                                            <template #suffix>
                                                <span> / 5.0</span>
                                            </template>
                                        </a-statistic>
                                    </a-comment>

                                </a-list-item>
                            </a-list>
                            <a-comment>
                                <a-avatar
                                        slot="avatar"
                                        src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                                />
                                <div slot="content">
                                    <a-form :form="form">
                                        <a-form-item>
                                            <a-textarea :rows="4"
                                                        v-decorator="['content', { rules: [{ required: true, message: '评论不能为空'}] }]"
                                            />
                                        </a-form-item>
                                        <a-form-item label="评分（可选）" :label-col="{ span: 2 }">
                                            <a-rate style="font-size: 20px" allowHalf
                                                    v-decorator="['rate', { rules: [{ required: false, message: '请评分'}] }]"
                                            >
                                                <a-icon slot="character" type="heart" />
                                            </a-rate>
                                        </a-form-item>
                                        <a-form-item>
                                            <a-tooltip title="在该酒店入住后方可撰写评论">
                                                <a-button html-type="submit"
                                                          :loading="submitting"
                                                          type="primary"
                                                          icon='edit'
                                                          @click="handleSubmit"
                                                          :disabled="!hasStayed"
                                                >
                                                    评论
                                                </a-button>
                                            </a-tooltip>
                                        </a-form-item>
                                    </a-form>

                                </div>
                            </a-comment>
                        </div>
                        <a-back-top />
                    </a-tab-pane>
                </a-tabs>
            </div>
        </a-layout-content>
    </a-layout>
</template>
<script>
import { mapGetters, mapActions, mapMutations } from 'vuex'
import moment from 'moment';
import RoomList from './components/roomList'
const columns = [
    {
        title: '订单号',
        dataIndex: 'id',
    },
    {
        title: '房型',
        dataIndex: 'roomType',
        scopedSlots: { customRender: 'roomType' }
    },
    {
        title: '数量',
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
        filters: [{ text: '已预订', value: '已预订' }, { text: '已撤销', value: '已撤销' }, { text: '已入住', value: '已入住' }],
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
    name: 'hotelDetail',
    components: {
        RoomList,
    },
    data() {
        return {
            submitting: false,
            columns,
            hasStayed: false,
            moment,
            form: this.$form.createForm(this, { name: 'writeComment' }),
        }
    },
    computed: {
        ...mapGetters([
            'currentHotelInfo',
            'userInfo',
            'userHotelOrderList',
            'currentHotelId',
            'commentsOfHotel',
        ])
    },
    async mounted() {
        this.set_currentHotelId(Number(this.$route.params.hotelId))
        this.getHotelById()
        this.getUserInfo()
        await this.getUserHotelOrders(this.currentHotelId)
        await this.getCommentsByHotelId()
        this.hasStayed = this.userHotelOrderList.filter(item => item.orderState === '已入住').length !== 0
    },
    beforeRouteUpdate(to, from, next) {
        this.set_currentHotelId(Number(to.params.hotelId))
        this.getHotelById()
        next()
    },
    methods: {
        ...mapMutations([
            'set_currentHotelId',
        ]),
        ...mapActions([
            'getHotelById',
            'getUserInfo',
            'cancelOrder',
            'getUserHotelOrders',
            'getCommentsByHotelId',
            'makeComment',
        ]),
        handleSubmit(e) {
            e.preventDefault();
            this.form.validateFieldsAndScroll((err, values) => {
                if (!err) {
                    const data = {
                        hotelId: this.currentHotelId,
                        authorId: this.userInfo.id,
                        author: this.userInfo.userName,
                        content: this.form.getFieldValue('content'),
                        rate: Number(this.form.getFieldValue('rate')),
                        datetime: moment().format("YYYY-MM-DD HH:mm:ss"),
                    }
                    this.makeComment(data)
                    this.form.setFieldsValue({
                        'content': '',
                        'rate': 0
                    })
                    this.submitting = false
                }
            });
        },
        confirmCancelOrder(orderId){
            this.cancelOrder(orderId)
        },
        cancelCancelOrder() {

        }
    },
}
</script>
<style scoped lang="less">
    .hotelDetailCard {
        padding: 50px 50px;
    }
    .hotel-info {
        display: flex;
        align-items: stretch;
        justify-content: flex-start;
        .info{
            padding: 10px 0;
            display: flex;
            flex-direction: column;
            margin-left: 20px;
            .items {
                display: flex;
                align-items: center;
                margin-bottom: 10px;
                .label{
                    margin-right: 10px;
                    font-size: 18px;
                }
                .value {
                    margin-right: 15px;
                    font-size: 15px;
                }
            }
        }
    }
</style>