package com.andrade.sms_formatter.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SmsDto {

    public record SmsRequest(
            @NotBlank @NotEmpty String message) {
    }

    public record SmsResponse(
            String sid,
            String name,
            String account,
            double amount,
            double tax,
            boolean isReceived,
            LocalDateTime date) {

    }
}
