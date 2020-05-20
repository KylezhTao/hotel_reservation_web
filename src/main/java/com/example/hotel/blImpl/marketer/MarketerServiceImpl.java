package com.example.hotel.blImpl.marketer;

import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.data.marketer.MarketerMapper;
import com.example.hotel.po.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class MarketerServiceImpl implements MarketerService {
    @Autowired
    MarketerMapper marketerMapper;

    @Override
    public List<User> getAllClients(){ return marketerMapper.getAllClients(); }
}
