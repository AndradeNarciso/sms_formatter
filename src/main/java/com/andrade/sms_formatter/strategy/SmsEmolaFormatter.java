package com.andrade.sms_formatter.strategy;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.andrade.sms_formatter.domain.Sms;

public class SmsEmolaFormatter implements SmsFormatter {

    @Override
    public Sms withdrawal(String withdrawalSms) {

        Pattern patternAmount = Pattern
                .compile("(?i)(Levantaste|Transferiste|Depositaste|Recebeste)\\s([0-9.,]+)MT");
        Matcher matcherAmount = patternAmount.matcher(body);

        if (matcherAmount.find()) {
            String valor = matcherAmount.group(2);

            valor = valor.replace(",", "");
            valor = valor.replace(",", ".");
            Double amount = Double.valueOf(valor);
        }

        Pattern patternAgent = Pattern.compile("(?i)agente\\s+([0-9]+)-([A-Za-zÀ-ÿ\\s]+)");
        Matcher matcherAgent = patternAgent.matcher(body);

        if (matcherAgent.find()) {
            String account = matcherAgent.group(1);
            String name = matcherAgent.group(2);
        }

        isReceived = false;
        throw new UnsupportedOperationException("Unimplemented method 'withdrawal'");
    }

    @Override
    public Sms transfer(String transferSms) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'transfer'");
    }

    @Override
    public Sms deposit(String depositSms) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public Sms reception(String receptionSms) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reception'");
    }

}
