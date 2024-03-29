package com.example.hotel.data.marketer;


import com.example.hotel.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MarketerMapper {
    List<User> getAllClients();

    int rechargeCredit(@Param("id") int id, @Param("credit") double credit);
}
