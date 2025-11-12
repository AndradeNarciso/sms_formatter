package com.andrade.sms_formatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class SmsFormatterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmsFormatterApplication.class, args);
	}

}
