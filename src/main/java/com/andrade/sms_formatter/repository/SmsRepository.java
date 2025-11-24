package com.andrade.sms_formatter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import com.andrade.sms_formatter.domain.Sms;

public interface  SmsRepository extends  JpaRepository<Sms, UUID> {
    
}
