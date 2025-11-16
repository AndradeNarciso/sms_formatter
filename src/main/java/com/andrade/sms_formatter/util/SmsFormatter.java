package com.andrade.sms_formatter.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.enums.OperationType;
import com.andrade.sms_formatter.enums.OperatorName;

@Component
public class SmsFormatter {

    
    public static Sms emolaFormatter(SmsRequest smsRequest) {
        String bady = smsRequest.message();

        String operatorName = OperatorName.MOVITEL.name();
        String operation = "";
        String sid = null;
        String name = null;
        String account = null;
        double amount = 0;
        double tax = 0;
        Boolean isReceived=null;
        LocalDateTime date = null;

        Pattern patternSid = Pattern.compile("ID da transacao:?[\\s]*([A-Za-z0-9\\.\\-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcherSid = patternSid.matcher(bady);
        if (matcherSid.find()) {
            sid = matcherSid.group(1);
        }
        

        Pattern patternName = Pattern.compile("nome:\\s+([A-Za-zÀ-ÖØ-öø-ÿ]+(?:\\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)*)",
                Pattern.CASE_INSENSITIVE);
        Matcher matcherNome = patternName.matcher(bady);
        if (matcherNome.find()) {
            name = matcherNome.group(1);
        }

        Pattern patternDate = Pattern.compile(
                "as\\s*(\\d{2}:\\d{2}:\\d{2})\\s+de\\s*(\\d{2}/\\d{2}/\\d{4})",
                Pattern.CASE_INSENSITIVE);
        Matcher matcherDate = patternDate.matcher(bady);

        if (matcherDate.find()) {
            String hour = matcherDate.group(1);
            String dateCatch = matcherDate.group(2);

            String dateStr = dateCatch + " " + hour;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            date = LocalDateTime.parse(dateStr, formatter);
        }

        if (bady.matches(".*\\bLevantaste\\b.*")) {
            operation = OperationType.WITHDRAWAL.name();
            isReceived = false;
        }
        if (bady.matches(".*\\bTransferiste\\b.*")) {
            operation = OperationType.TRANSFER.name();
            isReceived = false;
        }
        if (bady.matches(".*\\bRecebeste\\b.*Agente.*")) {
            operation = OperationType.DEPOSIT.name();
            isReceived = true;
        }
        if (bady.matches(".*\\bRecebeste\\b.*conta.*")) {
            operation = OperationType.RECEIPT.name();
            isReceived = true;
        }

        switch (operation) {
            case "WITHDRAWAL" -> {
                Pattern patternAmount = Pattern.compile("Levantaste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(bady);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                
                    amount = Double.parseDouble(raw);
                }

                Matcher matcherAgent = Pattern
                        .compile("Agente\\s+com\\s+codigo\\s+ID\\s+(\\d+)", Pattern.CASE_INSENSITIVE)
                        .matcher(bady);
                if (matcherAgent.find())
                    account = matcherAgent.group(1);

            }

            case "DEPOSIT" -> {
                Pattern patternAmount = Pattern.compile("Recebeste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(bady);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                
                    amount = Double.parseDouble(raw);
                }
                Matcher matcherAgent = Pattern
                        .compile("Agente\\s+com\\s+codigo\\s+ID\\s+(\\d+)", Pattern.CASE_INSENSITIVE)
                        .matcher(bady);
                if (matcherAgent.find())
                    account = matcherAgent.group(1);
            }

            case "TRANSFER" -> {
                Pattern patternAmount = Pattern.compile("Transferiste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(bady);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                
                    amount = Double.parseDouble(raw);
                }

                Pattern patternAccount = Pattern.compile("(?:para|de)\\s+conta\\s+(\\d+)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAccount = patternAccount.matcher(bady);
                if (matcherAccount.find()) {
                    account = matcherAccount.group(1);
                }

            }

            case "RECEIPT" -> {
                Pattern patternAmount = Pattern.compile("Recebeste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(bady);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                
                    amount = Double.parseDouble(raw);
                }
                Matcher matcherAccount = Pattern.compile("de\\s+conta\\s+(\\d+)", Pattern.CASE_INSENSITIVE).matcher(bady);
                if (matcherAccount.find())
                    account = matcherAccount.group(1);
            }

            default -> throw new AssertionError();
        }

       
        return Sms.builder()
        .operatorName(operatorName)
        .sid(sid)
        .name(name)
        .account(account)
        .amount(amount)
        .tax(tax)
        .isReceived(isReceived)
        .date(date)
        .operation(OperationType.valueOf(operation))
        .build();

    }

    public void mpesaFormatter(SmsRequest smsRequest) {

    }

}
