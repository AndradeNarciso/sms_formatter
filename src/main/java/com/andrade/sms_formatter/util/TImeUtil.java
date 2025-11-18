package com.andrade.sms_formatter.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TImeUtil {

     public static Long toEpochSeconds(LocalDateTime dateTime) {
        return dateTime.toEpochSecond(ZoneOffset.UTC);}
    
}
