package com.andrade.sms_formatter.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.andrade.sms_formatter.enums.OperationType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sms")
public class Sms {
    @Id
    @GeneratedValue
    private UUID id;
    private String sid;
    private String name;
    private String account;
    private int amount;
    private float tax;
    private boolean isReceived;
    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private OperationType operation;
}
