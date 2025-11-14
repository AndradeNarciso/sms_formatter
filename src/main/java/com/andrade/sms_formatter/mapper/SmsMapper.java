package com.andrade.sms_formatter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.dto.SmsDto.SmsResponse;

@Mapper(componentModel="spring" , unmappedTargetPolicy=ReportingPolicy.IGNORE)
public interface  SmsMapper {
    
    Sms toEntity(SmsRequest smsRequest);
    SmsResponse toDto(Sms sms);
}
