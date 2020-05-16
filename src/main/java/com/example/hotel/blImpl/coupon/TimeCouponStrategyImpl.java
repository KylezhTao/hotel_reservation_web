package com.example.hotel.blImpl.coupon;

import com.example.hotel.bl.coupon.CouponMatchStrategy;
import com.example.hotel.po.Coupon;
import com.example.hotel.vo.OrderVO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TimeCouponStrategyImpl implements CouponMatchStrategy {


    /**
     * 判断某个订单是否满足某种限时优惠策略
     * @param orderVO
     * @return
     */
    @Override
    public boolean isMatch(OrderVO orderVO, Coupon coupon) {
        try{
            LocalDateTime ld = LocalDateTime.now();
            return coupon.getCouponType() == 4 && ld.isAfter(coupon.getStartTime()) && ld.isBefore(coupon.getEndTime());
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
