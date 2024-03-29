package com.example.hotel.data.admin;

import com.example.hotel.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Mapper
@Repository
public interface AdminMapper {

    int addManager(User user);

    int addMarketer(User user);

    List<User> getAllManagers();

    List<User> getAllUsers();
}
