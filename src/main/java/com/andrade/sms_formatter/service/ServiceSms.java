package com.andrade.sms_formatter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.dto.SmsDto.SmsResponse;
import com.andrade.sms_formatter.mapper.SmsMapper;
import com.andrade.sms_formatter.util.SmsFormatter;

@Service
public class ServiceSms {

    @Autowired
    private SmsFormatter smsFormatter;
    @Autowired
    private SmsMapper smsMapper;

    public List<SmsResponse> saveAndReturnResponseServiceToEmola(List<SmsRequest> smsRequests) {
        List<SmsResponse> listFormatted = new ArrayList<>();

        for (SmsRequest sms : smsRequests) {
            Sms smsFormated=smsFormatter.emolaFormatter(sms);
                if(smsFormated.getOperation().equals("DEPOSIT") ||smsFormated.getOperation().equals("RECEIPT") ){
                    smsFormated.setReceived(true);
                }

                listFormatted.add(smsMapper.toDto(smsFormated));
        }

        return listFormatted;
    }

}
