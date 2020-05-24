package com.example.hotel.blImpl.order;

import com.example.hotel.bl.hotel.HotelService;
import com.example.hotel.bl.order.OrderService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.Order;
import com.example.hotel.po.User;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    private final static String RESERVE_ERROR = "预订失败";
    private final static String ANNUL_ERROR = "取消订单失败";
    private final static String ROOMNUM_LACK = "预订房间数量剩余不足";
    private final static String LOW_CREDIT = "您的信用值过低，无法生成订单";
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HotelService hotelService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMapper accountMapper;

    @Override
    public ResponseVO addOrder(OrderVO orderVO) {
        User user = accountService.getUserInfo(orderVO.getUserId());
        if(user.getCredit() < 0)
            return ResponseVO.buildFailure(LOW_CREDIT);
        int reserveRoomNum = orderVO.getRoomNum();
        int curNum = hotelService.getRoomCurNum(orderVO.getHotelId(),orderVO.getRoomType());
        if(reserveRoomNum>curNum){
            return ResponseVO.buildFailure(ROOMNUM_LACK);
        }
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(System.currentTimeMillis());
            String curdate = sf.format(date);
            orderVO.setCreateDate(curdate);
            orderVO.setOrderState("已预订");
            orderVO.setClientName(user.getUserName());
            orderVO.setPhoneNumber(user.getPhoneNumber());
            orderVO.setLatestExecTime(orderVO.getCheckInDate() + " 23:00:00");
            Order order = new Order();
            BeanUtils.copyProperties(orderVO,order);
            orderMapper.addOrder(order);
            hotelService.updateRoomInfo(orderVO.getHotelId(),orderVO.getRoomType(),orderVO.getRoomNum());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(RESERVE_ERROR);
        }
       return ResponseVO.buildSuccess(true);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderMapper.getAllOrders();
    }

    @Override
    public List<Order> getUserOrders(int userid) {
        return orderMapper.getUserOrders(userid);
    }

    @Override
    public List<Order> getManagerOrders(int userid) {
        return orderMapper.getManagerOrders(userid);
    }

    @Override
    public List<Order> getUserHotelOrders(int userid, int hotelid) {
        return orderMapper.getUserHotelOrders(userid, hotelid);
    }

    @Override
    public ResponseVO annulOrder(int orderid) {
        try{
            Order order = orderMapper.getOrderById(orderid);
            int roomNum = -order.getRoomNum();
            hotelService.updateRoomInfo(order.getHotelId(), order.getRoomType(), roomNum);
            orderMapper.annulOrder(orderid);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime latestExecTime = LocalDateTime.parse(order.getLatestExecTime(), fmt);
            Duration duration = Duration.between(LocalDateTime.now(), latestExecTime);
            CreditRecord record = new CreditRecord();
            record.setOrderId(orderid);
            record.setClientId(order.getUserId());
            record.setDatetime(LocalDateTime.now());
            record.setAction("订单撤销");
            User user = accountService.getUserInfo(order.getUserId());
            if(duration.toHours() < 6){
                record.setChange(-order.getPrice()/2);
            }else{
                record.setChange(0);
            }
            record.setResult(user.getCredit() + record.getChange());
            orderMapper.insertRecord(record);
            accountMapper.updateCredit(user.getId(),record.getResult());

        }catch(Exception e){
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(ANNUL_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }

    @Override
    public ResponseVO updateOrder(int orderid, OrderVO orderVO){
        try{
            Order order = orderMapper.getOrderById(orderid);
            User user = accountService.getUserInfo(order.getUserId());
            CreditRecord record = new CreditRecord();
            record.setOrderId(orderid);
            record.setClientId(order.getUserId());
            record.setDatetime(LocalDateTime.now());
            switch (orderVO.getOrderState()){
                case "已执行":
                    hotelService.updateRoomInfo(order.getHotelId(),order.getRoomType(),-order.getRoomNum());
                    orderMapper.updateOrder(orderid,orderVO.getOrderState());
                    record.setAction("订单执行");
                    record.setChange(order.getPrice());
                    break;
                case "已撤销":
                    orderMapper.updateOrder(orderid,orderVO.getOrderState());
                    record.setAction("订单申诉");
                    record.setChange(orderVO.getIsHalf()==1?order.getPrice()/2:order.getPrice());
                    break;
                case "异常":
                    orderMapper.updateOrder(orderid,orderVO.getOrderState());
                    record.setAction("订单异常");
                    record.setChange(-order.getPrice());

            }
            double res = record.getChange() + user.getCredit();
            record.setResult(res);
            orderMapper.insertRecord(record);
            accountMapper.updateCredit(user.getId(),res);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure("更新订单信息失败");
        }
        return ResponseVO.buildSuccess(true);
    }
}
