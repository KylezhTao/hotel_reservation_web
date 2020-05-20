<template>
  <div class="hotelList">
      <a-tabs>
          <a-tab-pane tab="卡片" key="1">
              <a-layout>
                  <a-layout-content style="min-width: 800px">
                      <a-spin :spinning="hotelListLoading">
                          <div class="card-wrapper">
                              <HotelCard :hotel="item" v-for="item in hotelList" :key="item.index" @click.native="jumpToDetails(item.id)"></HotelCard>
                              <div v-for="item in emptyBox" :key="item.name" class="emptyBox ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
                              </div>
                              <a-pagination showQuickJumper :total="hotelList.totalElements" :defaultCurrent="1" @change="pageChange"></a-pagination>
                          </div>
                      </a-spin>
                  </a-layout-content>
              </a-layout>
          </a-tab-pane>
          <a-tab-pane tab="列表" key="2">
              <div class="table-operations" style="width: 100%; text-align: left; margin:20px 0">
                  <a-button @click="myOrdered"><a-icon type="filter"/>
                      我预订过的酒店
                  </a-button>
                  <a-divider type="vertical"></a-divider>
                  <a-button @click="reset"><a-icon type="unordered-list"/>所有酒店</a-button>
              </div>
              <a-table
                      :columns="columns1"
                      :dataSource="isFilter?res:hotelList"
                      bordered
              >

                  <div
                          slot="filterDropdown"
                          slot-scope="{ setSelectedKeys, selectedKeys, confirm, clearFilters, column }"
                          style="padding: 8px"
                  >
                      <a-input
                              v-ant-ref="c => (searchInput = c)"
                              :placeholder="`Search ${column.dataIndex}`"
                              :value="selectedKeys[0]"
                              style="width: 188px; margin-bottom: 8px; display: block;"
                              @change="e => setSelectedKeys(e.target.value ? [e.target.value] : [])"
                              @pressEnter="() => handleSearch(selectedKeys, confirm, column.dataIndex)"
                      />
                      <a-button
                              type="primary"
                              icon="search"
                              size="small"
                              style="width: 90px; margin-right: 8px"
                              @click="() => handleSearch(selectedKeys, confirm, column.dataIndex)"
                      >
                          搜索
                      </a-button>
                      <a-button size="small" style="width: 90px" @click="() => handleReset(clearFilters)">
                          重置
                      </a-button>
                  </div>
                  <a-icon
                          slot="filterIcon"
                          slot-scope="filtered"
                          type="search"
                          :style="{ color: filtered ? '#108ee9' : undefined }"
                  />
                  <template slot="customRender" slot-scope="text, record, index, column">
                      <span v-if="searchText && searchedColumn === column.dataIndex">
                          <template
                                  v-for="(fragment, i) in text.toString().split(new RegExp(`(?<=${searchText})|(?=${searchText})`, 'i'))">
                              <mark
                                      v-if="fragment.toLowerCase() === searchText.toLowerCase()"
                                      :key="i"
                                      class="highlight"
                              >{{ fragment }}
                              </mark>
                              <template v-else>{{ fragment }}</template>
                          </template>
                      </span>
                      <template v-else>
                          {{ text }}
                      </template>
                  </template>
                  <a-tag v-if="text!==''" slot="roomTypes" slot-scope="text">{{text}}</a-tag>
                  <a-tag v-else>暂无房间</a-tag>
                  <a-tag :color="text==='Five'?'orange':text==='Four'?'blue':'green'" slot="hotelStar" slot-scope="text">{{text}}</a-tag>
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
                      <a-button size="small" type='info' icon="zoom-in" @click.native="jumpToDetails(record.id)">查看详情</a-button>
                  </span>

              </a-table>
          </a-tab-pane>
      </a-tabs>

  </div>
</template>
<script>
import HotelCard from './components/hotelCard'
import { mapGetters, mapActions, mapMutations } from 'vuex'
function parse(x){
    if(x==='Three')
        return 3
    else if(x==='Four')
        return 4
    else
        return 5
}
const columns1 = [
    {
        title: '酒店名',
        dataIndex: 'name',
        scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
        },
        onFilter: (value, record) =>
            record.name
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => {
                    this.searchInput.focus();
                }, 0);
            }
        },
    },
    {
        title: '商圈',
        dataIndex: 'bizRegion',
        scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
        },
        onFilter: (value, record) =>
            record.name
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => {
                    this.searchInput.focus();
                }, 0);
            }
        },
    },
    {
        title: '地址',
        dataIndex: 'address',
        scopedSlots: {
            filterDropdown: 'filterDropdown',
            filterIcon: 'filterIcon',
            customRender: 'customRender',
        },
        onFilter: (value, record) =>
            record.name
                .toString()
                .toLowerCase()
                .includes(value.toLowerCase()),
        onFilterDropdownVisibleChange: visible => {
            if (visible) {
                setTimeout(() => {
                    this.searchInput.focus();
                }, 0);
            }
        },
    },
    {
        title: '酒店星级',
        dataIndex: 'hotelStar',
        scopedSlots:{customRender: 'hotelStar'},
        filters: [
            {
                text: '三星级',
                value: '3',
            },
            {
                text: '四星级',
                value: '4',
            },
            {
                text: '五星级',
                value: '5',
            },
        ],
        filterMultiple: true,
        onFilter: (value, record) => parse(record.hotelStar) === Number(value),
        sorter: (a, b) => parse(a.hotelStar) - parse(b.hotelStar),
        sortDirections: ['descend', 'ascend'],
    },
    {
        title: '评分',
        dataIndex: 'rate',
        scopedSlots:{customRender: 'rate'},
        filters: [
            {
                text: '4.5 ~ 5.0',
                value: '4.5,5.0',
            },
            {
                text: '4.0 ~ 4.5',
                value: '4.0,4.5',
            },
            {
                text: '3.5 ~ 4.0',
                value: '3.5,4.0',
            },
            {
                text: '3.0 ~ 3.5',
                value: '3.0,3.5',
            },
        ],
        filterMultiple: true,
        onFilter: (value, record) => record.rate >= Number(value.split(',')[0]) && record.rate <= Number(value.split(',')[1]),
        sorter: (a, b) => a.rate - b.rate,
        sortDirections: ['descend', 'ascend'],
    },
    {
        title: '现有房型',
        dataIndex: 'roomTypes',
        scopedSlots:{customRender: 'roomTypes'},
        filters: [
            {
                text: '大床房',
                value: '大床房',
            },
            {
                text: '双床房',
                value: '双床房',
            },
            {
                text: '家庭房',
                value: '家庭房',
            },
        ],
        filterMultiple: false,
        onFilter: (value, record) => record.roomTypes.indexOf(value) !== -1,

    },
    {
        title: '操作',
        key: 'action',
        scopedSlots: { customRender: 'action' },
    },
];
export default {
  name: 'home',
  components: {
    HotelCard,
  },
  data(){
    return{
        res: [],
        searchText: '',
        searchInput: null,
        searchedColumn: '',
        isFilter: false,
      columns1,
      emptyBox: [{ name: 'box1' }, { name: 'box2'}, {name: 'box3'}]
    }
  },
  async mounted() {
      await this.getHotelList()
      await this.getUserOrders()
  },
  computed: {
    ...mapGetters([
      'hotelList',
      'userOrderList',
      'hotelListLoading'
    ])
  },
  methods: {
    ...mapMutations([
      'set_hotelListParams',
      'set_hotelListLoading'
    ]),
    ...mapActions([
      'getHotelList',
      'getUserOrders',
    ]),

      pageChange(page, pageSize) {
          const data = {
              pageNo: page - 1
          }
          this.set_hotelListParams(data)
          this.set_hotelListLoading(true)
          this.getHotelList()
      },
      jumpToDetails(id){
          this.$router.push({ name: 'hotelDetail', params: { hotelId: id }})
      },
      myOrdered(){
          this.res = this.hotelList.filter(item => {
              for(let order of this.userOrderList){
                  if(order.hotelId===item.id)
                      return true;
              }
              return false;
          })
          this.isFilter = true
      },
      reset(){
          this.isFilter = false
      },
      handleSearch(selectedKeys, confirm, dataIndex) {
          confirm();
          this.searchText = selectedKeys[0];
          this.searchedColumn = dataIndex;
      },

      handleReset(clearFilters) {
          clearFilters();
          this.searchText = '';
      },
  },
}
</script>
<style scoped lang="less">
   .highlight {
      background-color: rgb(255, 192, 105);
      padding: 0px;
  }

  .hotelList {
    text-align: center;
    padding: 50px 0;
    .emptyBox {
      height: 0;
      margin: 10px 10px
    }
    .card-wrapper{
      display: flex;
      justify-content: space-around;
      flex-wrap: wrap;
      flex-grow: 3;
      min-height: 800px
    }
    .card-wrapper .card-item {
      margin: 30px;
      position: relative;
      height: 188px;
    }
  }
</style>