package com.example.hotel.data.hotel;

import com.example.hotel.po.Hotel;
import com.example.hotel.vo.HotelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface HotelMapper {

    int insertHotel(Hotel hotel);

    List<HotelVO> selectAllHotel();

    HotelVO selectById(@Param("id") Integer id);

    int updateHotel(@Param("id") int id, @Param("address") String address, @Param("bizRegion") String bizRegion,
                    @Param("hotelStar") String hotelStar, @Param("hotelService") String hotelService, @Param("description") String description);
}
