<template>
  <a-card hoverable class="hotelCard ant-col-xs-7 ant-col-lg-5 ant-col-xxl-3">
    <img
      alt="example"
      src="@/assets/cover.jpeg"
      slot="cover"
      referrerPolicy="no-referrer"
    />
    <a-tooltip :title="hotel.title" placement="top">
      <a-card-meta :title="hotel.name">
      <template slot="description">
        <a-statistic :value="hotel.rate"
                     :precision="2"
                     style="text-align: center"

        >
          <template #prefix>
            <a-rate :value="hotel.rate" disabled allowHalf>
              <a-icon slot="character" type="heart" />
            </a-rate>
          </template>
          <template #suffix>
            <span> / 5</span>

          </template>
        </a-statistic>
      </template>
    </a-card-meta>
    </a-tooltip>
  </a-card>
</template>
<script>
export default {
  name:'',
  props: {
    hotel: {}
  },
  data() {
    return{

    }
  },
}
</script>
<style scoped lang="less">
    .hotelCard {
        margin: 10px 10px;
        min-width: 180px;
        max-height: 450px;
        img {
          height: 360px;
        }
    }
</style>

<style lang="less">
.hotelCard{
  .ant-card-body{
    padding: 12px
  }
}

.ant-card-hoverable:hover {
  box-shadow: 0 2px 8px rgba(0,0,0,0.4)
}


</style>