package com.andrade.sms_formatter.domain;

import java.util.UUID;

import com.andrade.sms_formatter.enums.OperationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "sms")

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sms {
    @Id
    @GeneratedValue
    private UUID id;
    private String operatorName;
    private String sid;
    private String name;
    private String account;
    private Double amount;
    private Double tax;
    private Boolean isReceived;
    private Long date;

    @Enumerated(EnumType.STRING)
    private OperationType operation;
}
