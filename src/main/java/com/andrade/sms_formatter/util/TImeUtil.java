package com.andrade.sms_formatter.util;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class TImeUtil {

        public static Long toEpochSeconds(LocalDateTime dateTime) {
                if (dateTime != null) {
                        return dateTime.toEpochSecond(ZoneOffset.UTC);
                }

                return null;
        }

}
