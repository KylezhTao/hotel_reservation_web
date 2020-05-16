package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.data.user.AccountMapper;
import com.example.hotel.po.Coupon;
import com.example.hotel.po.User;
import com.example.hotel.vo.CouponVO;
import com.example.hotel.vo.HotelTargetMoneyCouponVO;
import com.example.hotel.vo.OrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TargetMoneyCouponStrategyImpl implements CouponMatchStrategy {
    @Autowired
    private AccountMapper accountMapper;

    /**
     * 判断某个订单是否满足某种满减金额优惠策略,生日特惠也是满减方式
     * @param orderVO
     * @param coupon
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        try{
            if(coupon.getCouponType() == 3 && orderVO.getPrice() >= coupon.getTargetMoney()){
                return true;
            }
            else if(coupon.getCouponType() == 1){
                User user = accountMapper.getAccountById(orderVO.getUserId());
                LocalDate today = LocalDate.now();
                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");
                String text1 = today.format(fmt);
                String text2 = user.getBirthday().format(fmt);
                return text1.equals(text2);
            }else
                return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
