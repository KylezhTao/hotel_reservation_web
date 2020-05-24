package com.example.hotel.bl.order;

import com.example.hotel.po.Order;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;

import java.util.List;


public interface OrderService {

    ResponseVO addOrder(OrderVO orderVO);

    List<Order> getAllOrders();

    List<Order> getUserOrders(int userid);

    List<Order> getManagerOrders(int userid);

    List<Order> getUserHotelOrders(int userid, int hotelid);

    ResponseVO annulOrder(int orderid);

    ResponseVO updateOrder(int orderid, OrderVO orderVO);



}
