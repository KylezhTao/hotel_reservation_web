package com.example.hotel.blImpl.marketer;

import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.bl.user.AccountService;
import com.example.hotel.data.marketer.MarketerMapper;
import com.example.hotel.data.order.OrderMapper;
import com.example.hotel.po.CreditRecord;
import com.example.hotel.po.User;
import com.example.hotel.vo.ResponseVO;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MarketerServiceImpl implements MarketerService {
    private final static String UPDATE_ERROR = "充值失败";
    @Autowired
    MarketerMapper marketerMapper;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    AccountService accountService;

    @Override
    public List<User> getAllClients(){ return marketerMapper.getAllClients(); }

    @Override
    public ResponseVO rechargeCredit(int id, double credit) {
        try {
            marketerMapper.rechargeCredit(id, credit);
            User user = accountService.getUserInfo(id);
            CreditRecord record = new CreditRecord();
            record.setClientId(id);
            record.setDatetime(LocalDateTime.now());
            record.setAction("充值");
            record.setChange(credit);
            record.setResult(user.getCredit());
            orderMapper.insertRecord(record);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseVO.buildFailure(UPDATE_ERROR);
        }
        return ResponseVO.buildSuccess(true);
    }


}
