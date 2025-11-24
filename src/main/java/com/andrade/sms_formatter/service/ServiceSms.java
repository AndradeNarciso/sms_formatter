package com.andrade.sms_formatter.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.dto.SmsDto.SmsResponse;
import com.andrade.sms_formatter.mapper.SmsMapper;
import com.andrade.sms_formatter.repository.SmsRepository;
import com.andrade.sms_formatter.util.SmsFormatterUtil;

import jakarta.transaction.Transactional;

@Service
public class ServiceSms {

    @Autowired
    private SmsFormatterUtil smsFormatterService;

    @Autowired
    private SmsMapper smsMapperService;

    @Autowired
    private SmsRepository smsRepositoryService;

    @Transactional(rollbackOn = Exception.class)
    public List<SmsResponse> saveAndReturnResponseServiceToEmola(List<SmsRequest> smsRequests) {
        List<SmsResponse> listFormatted = new ArrayList<>();

        for (SmsRequest sms : smsRequests) {
            listFormatted
                    .add(smsMapperService.toDto(smsRepositoryService.save(smsFormatterService.emolaFormatter(sms))));
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
                            System.err.println("There is a problem at acessing emola field");
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());

    }

    @Transactional(rollbackOn = Exception.class)
    public List<SmsResponse> saveAndReturnResponseServiceToMpesa(List<SmsRequest> smsRequests) {
        List<SmsResponse> listFormatted = new ArrayList<>();

        for (SmsRequest sms : smsRequests) {
            listFormatted
                    .add(smsMapperService.toDto(smsRepositoryService.save(smsFormatterService.mpesaFormatter(sms))));
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


    public   List <Sms> getAllSmsSaved(){
        return smsRepositoryService.findAll();
    }

}
