package com.andrade.sms_formatter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.dto.SmsDto.SmsResponse;
import com.andrade.sms_formatter.service.ServiceSms;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("api/v1/sms/")
public class SmsController {
    
    @Autowired
    private ServiceSms serviceSms;

    @PostMapping("/mpesa")
    public ResponseEntity<List<SmsResponse>> withdrawSmS(@RequestBody List<SmsRequest> smsRequest ) {
        return ResponseEntity.status(200).body(serviceSms.saveAndReturnResponseServiceToMpesa(smsRequest));
    }
    
    @PostMapping("/emola")
    public ResponseEntity<List<SmsResponse>> depositSmS(@RequestBody List<SmsRequest> smsRequest ) {
        return ResponseEntity.status(200).body(serviceSms.saveAndReturnResponseServiceToEmola(smsRequest));
    }

    @GetMapping("")
    public List<Sms> getMethodName(@RequestParam String param) {
        return serviceSms.getAllSmsSaved();
    }
    

}
