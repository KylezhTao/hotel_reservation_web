package com.example.hotel.controller.marketer;


import com.example.hotel.bl.marketer.MarketerService;
import com.example.hotel.vo.ResponseVO;
import com.example.hotel.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api/marketer")
public class MarketerController {
    @Autowired
    MarketerService marketerService;

    @PostMapping("/{clientId}/rechargeCredit")
    public ResponseVO rechargeCredit(@RequestBody UserVO userVO, @PathVariable int clientId){
        return ResponseVO.buildSuccess(marketerService.rechargeCredit(clientId, userVO.getCredit()));
    }

    @PostMapping("/getAllClients")
    public ResponseVO getAllClients(){
        return ResponseVO.buildSuccess(marketerService.getAllClients());
    }
}
