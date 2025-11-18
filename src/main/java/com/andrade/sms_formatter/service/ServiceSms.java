package com.andrade.sms_formatter.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.dto.SmsDto.SmsResponse;
import com.andrade.sms_formatter.mapper.SmsMapper;
import com.andrade.sms_formatter.util.SmsFormatterUtil;


@Service
public class ServiceSms {

    @Autowired
    private SmsFormatterUtil smsFormatter;
    @Autowired
    private SmsMapper smsMapper;

    public List<SmsResponse> saveAndReturnResponseServiceToEmola(List<SmsRequest> smsRequests) {
        List<SmsResponse> listFormatted = new ArrayList<>();

        for (SmsRequest sms : smsRequests) {
            listFormatted.add(smsMapper.toDto(smsFormatter.emolaFormatter(sms)));
        }

        return listFormatted.stream()
        .filter(r -> r != null)                     
        .filter(smsResponse -> {
            for (Field field : smsResponse.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    if (field.get(smsResponse) == null) {
                        return false;
                    }
                } catch (IllegalAccessException e) {
                    System.err.println("There is a problem at acessing mpesa field");
                }
            }
            return true;
        })
        .collect(Collectors.toList());


    }





}
