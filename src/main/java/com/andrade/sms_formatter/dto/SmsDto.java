package com.andrade.sms_formatter.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SmsDto {

        public record SmsRequest(
                        @NotBlank @NotEmpty String message) {
        }

        public record SmsResponse(
                        String operation,
                        String sid,
                        String name,
                        String account,
                        double amount,
                        double tax,
                        boolean isReceived,
                        Long date) {

        }
}
