package com.example.hotel.controller.marketer;


import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api/marketer")
public class MarketerController {
    @Autowired
    MarketerService marketerService;

    @PostMapping("/getAllClients")
    public ResponseVO getAllClients(){
        return ResponseVO.buildSuccess(marketerService.getAllClients());
    }
}
