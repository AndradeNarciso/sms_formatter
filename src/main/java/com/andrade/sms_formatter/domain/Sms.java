package com.andrade.sms_formatter.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
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
}
