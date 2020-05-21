package com.example.hotel.bl.marketer;

import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;

import java.util.List;

public interface MarketerService {

    List<User> getAllClients();

    ResponseVO rechargeCredit(int id, double credit);
}
