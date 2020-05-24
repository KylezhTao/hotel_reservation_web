package com.example.hotel.bl.user;

import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.User;
import com.example.hotel.vo.*;

import java.util.List;

public interface AccountService {

    ResponseVO registerAccount(UserVO userVO);

    User login(UserForm userForm);

    User getUserInfo(int id);

    ResponseVO updateUserInfo(int id, String password, String username, String phonenumber);

    ResponseVO makeComment(int id, CommentVO commentVO);

    List<CreditRecord> retrieveUserCreditRecords(int id);


}
