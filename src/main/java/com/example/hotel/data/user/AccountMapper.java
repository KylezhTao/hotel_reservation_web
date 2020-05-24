package com.example.hotel.data.user;

import com.example.hotel.po.Comment;
import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Mapper
@Repository
public interface AccountMapper {

     int createNewAccount(User user);

     User getAccountByName(@Param("email") String email);

     User getAccountById(@Param("id") int id);

     int updateAccount(@Param("id") int id, @Param("password") String password, @Param("userName") String username, @Param("phoneNumber") String phonenumber);

     int insertComment(Comment comment);

     int updateCredit(@Param("id") int id,@Param("credit") double credit);

     List<CreditRecord> selectUserCreditRecords(@Param("clientId") int id);


}
