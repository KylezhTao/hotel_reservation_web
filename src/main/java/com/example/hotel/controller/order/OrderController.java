package com.example.hotel.controller.order;

import com.example.hotel.bl.order.OrderService;
import com.example.hotel.vo.OrderVO;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/addOrder")
    public ResponseVO reserveHotel(@RequestBody OrderVO orderVO){
        return orderService.addOrder(orderVO);
    }

    @GetMapping("/getAllOrders")
    public ResponseVO retrieveAllOrders(){
        return ResponseVO.buildSuccess(orderService.getAllOrders());
    }

    @GetMapping("/{userid}/getMyOrders")
    public  ResponseVO retrieveManagerOrders(@PathVariable int userid){
        return ResponseVO.buildSuccess(orderService.getManagerOrders(userid));
    }

    @GetMapping("/{userid}/getUserOrders")
    public  ResponseVO retrieveUserOrders(@PathVariable int userid){
        return ResponseVO.buildSuccess(orderService.getUserOrders(userid));
    }

    @GetMapping("/{userid}/{hotelid}/getUserHotelOrders")
    public  ResponseVO retrieveUserOrders(@PathVariable int userid, @PathVariable int hotelid){
        return ResponseVO.buildSuccess(orderService.getUserHotelOrders(userid, hotelid));
    }

    @GetMapping("/{orderid}/annulOrder")
    public ResponseVO annulOrder(@PathVariable int orderid){
        return orderService.annulOrder(orderid);
    }

    @PostMapping("/{orderid}/update")
    public ResponseVO updateOrder(@PathVariable int orderid, @RequestBody OrderVO orderVO){
        return orderService.updateOrder(orderid, orderVO);
    }


}
