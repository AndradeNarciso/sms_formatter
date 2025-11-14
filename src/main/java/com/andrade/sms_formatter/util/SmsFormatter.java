package com.andrade.sms_formatter.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;


public class SmsFormatter {
    @Autowired 
    private Pattern pattern;
    @Autowired
    private Matcher matcher; 

    public void mpesaFormatter(){}

    public void emolaFormatter(){}
    
}
