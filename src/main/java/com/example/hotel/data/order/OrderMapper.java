package com.example.hotel.data.order;

import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.Order;
import com.example.hotel.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface OrderMapper {

    int addOrder(Order order);

    List<Order> getAllOrders();

    List<Order> getUserOrders(@Param("userid") int userid);

    List<Order> getManagerOrders(@Param("userid") int userid);

    List<Order> getUserHotelOrders(@Param("userid") int userid, @Param("hotelid") int hotelid);

    int annulOrder(@Param("orderid") int orderid);

    Order getOrderById(@Param("orderid") int orderid);

    int updateOrder(@Param("id") int id, @Param("orderState") String orderState);

    int insertRecord(CreditRecord record);

}
