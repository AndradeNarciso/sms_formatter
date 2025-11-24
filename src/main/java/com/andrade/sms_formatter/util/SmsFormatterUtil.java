package com.andrade.sms_formatter.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.andrade.sms_formatter.domain.Sms;
import com.andrade.sms_formatter.dto.SmsDto.SmsRequest;
import com.andrade.sms_formatter.enums.OperationType;
import com.andrade.sms_formatter.enums.OperatorName;

@Component
public class SmsFormatterUtil {

    public Sms emolaFormatter(SmsRequest smsRequest) {
        String body = smsRequest.message();

        String operatorName = OperatorName.MOVITEL.name();
        String operation = null;
        String sid = null;
        String name = null;
        String account = null;
        Double amount = null;
        Double tax = null;
        Boolean isReceived = null;
        LocalDateTime timeStamp = null;

        Pattern patternSid = Pattern.compile("ID da transacao:?[\\s]*([A-Za-z0-9\\.\\-]+)", Pattern.CASE_INSENSITIVE);
        Matcher matcherSid = patternSid.matcher(body);
        if (matcherSid.find()) {
            sid = matcherSid.group(1);
        }

        Pattern patternName = Pattern.compile(
                "(?i)nome:?" +
                        "\\s+" +
                        "([A-Za-zÀ-ÖØ-öø-ÿ]+(?:\\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)*)" +
                        "\\s+(?=as\\b)",
                Pattern.CASE_INSENSITIVE);

        Matcher matcherName = patternName.matcher(body);
        if (matcherName.find()) {
            name = matcherName.group(1);
        }

        Pattern patternDate = Pattern.compile(
                "as\\s*(\\d{2}:\\d{2}:\\d{2})\\s+de\\s*(\\d{2}/\\d{2}/\\d{4})",
                Pattern.CASE_INSENSITIVE);
        Matcher matcherDate = patternDate.matcher(body);

        if (matcherDate.find()) {
            String hour = matcherDate.group(1);
            String dateCatch = matcherDate.group(2);

            String dateStr = dateCatch + " " + hour;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            timeStamp = LocalDateTime.parse(dateStr, formatter);
        }

        if (body.matches(".*\\bLevantaste\\b.*")) {
            operation = OperationType.LEVANTAMENTO.name();

        } else if (body.matches(".*\\bTransferiste\\b.*")) {
            operation = OperationType.TRANSFERENCIA.name();

        } else if (body.matches(".*\\bRecebeste\\b.*Agente.*")) {
            operation = OperationType.DEPOSITO.name();

        } else if (body.matches(".*\\bRecebeste\\b.*conta.*")) {
            operation = OperationType.RECEPCAO.name();

        }

        if (operation == null) {
            return null;
        }

        switch (operation) {
            case "LEVANTAMENTO" -> {
                isReceived = false;
                Pattern patternAmount = Pattern.compile("Levantaste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");

                    amount = Double.valueOf(raw);

                }

                Matcher matcherAgent = Pattern
                        .compile("Agente\\s+com\\s+codigo\\s+ID\\s+(\\d+)\\s*-\\s*([A-Za-zÀ-ÿ\\s]+?)(?=\\s+aos|\\.|$)",
                                Pattern.CASE_INSENSITIVE)
                        .matcher(body);
                if (matcherAgent.find()) {
                    account = matcherAgent.group(1);
                    name = matcherAgent.group(2).trim();
                }

                Pattern patternTax = Pattern.compile("(?i)Taxa:\\s*([\\d.,]+)\\s*MT");
                Matcher matcherTax = patternTax.matcher(body);

                if (matcherTax.find()) {
                    String raw = matcherTax.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                    tax = Double.valueOf(raw);
                }
            }

            case "DEPOSITO" -> {
                isReceived = true;
                Pattern patternAmount = Pattern.compile("Recebeste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                    amount = Double.valueOf(raw);
                }

                Matcher matcherAgent = Pattern
                        .compile("Agente\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ\\s0-9\\-]+?)(?=\\s+aos|\\.|$)",
                                Pattern.CASE_INSENSITIVE)
                        .matcher(body);
                if (matcherAgent.find()) {
                    account = matcherAgent.group(1);
                    name = matcherAgent.group(2).trim();
                }
            }

            case "TRANSFERENCIA" -> {
                isReceived = false;
                Pattern patternAmount = Pattern.compile("Transferiste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                    amount = Double.valueOf(raw);
                }

                Pattern patternAccount = Pattern.compile(
                        "para\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ\\s]+?)(?=\\s+aos|\\s+\\d{1,2}/\\d{1,2}/\\d{2}|\\.)",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcherAccount = patternAccount.matcher(body);
                if (matcherAccount.find()) {
                    account = matcherAccount.group(1);
                    name = matcherAccount.group(2).trim();
                }

                Pattern patternTax = Pattern.compile("(?i)Taxa:\\s*([\\d.,]+)\\s*MT");
                Matcher matcherTax = patternTax.matcher(body);

                if (matcherTax.find()) {
                    String raw = matcherTax.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                    tax = Double.valueOf(raw);
                }
            }

            case "RECEPCAO" -> {
                isReceived = true;
                Pattern patternAmount = Pattern.compile("Recebeste\\s+([\\d.,]+)(?=MT)", Pattern.CASE_INSENSITIVE);
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String raw = matcherAmount.group(1);
                    raw = raw.replaceAll("\\.(?=\\d{3})", "");
                    raw = raw.replaceAll(",(?=\\d{3})", "");
                    raw = raw.replace(",", ".");
                    amount = Double.valueOf(raw);
                }

                Pattern patternAccount = Pattern.compile(
                        "(?:para|de)\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ\\s]+?)(?=\\s+aos|\\s+\\d{1,2}/\\d{1,2}/\\d{2}|\\.)",
                        Pattern.CASE_INSENSITIVE);
                Matcher matcherAccount = patternAccount.matcher(body);
                if (matcherAccount.find()) {
                    account = matcherAccount.group(1);
                    name = matcherAccount.group(2).trim();
                }
            }
        }

        return Sms.builder()
                .operatorName(operatorName)
                .sid(sid)
                .name(name)
                .account(account)
                .amount(amount)
                .tax(tax)
                .isReceived(isReceived)
                .date(TImeUtil.toEpochSeconds(timeStamp))
                .operation(OperationType.valueOf(operation))
                .build();
    }

    public Sms mpesaFormatter(SmsRequest smsRequest) {
        String body = smsRequest.message();

        String operatorName = OperatorName.VODACOM.name();
        String operation = null;
        String sid = null;
        String name = null;
        String account = null;
        Double amount = null;
        Double tax = null;
        Boolean isReceived = null;
        LocalDateTime timeStamp = null;

        Pattern patternSid = Pattern.compile("Confirmado\\s([A-Z0-9]+)");
        Matcher matcherSid = patternSid.matcher(body);
        if (matcherSid.find()) {
            sid = matcherSid.group(1);
        }

        Pattern patternDateTime = Pattern.compile(
                "aos?\\s*(\\d{1,2}/\\d{1,2}/\\d{2})\\s+as\\s*(\\d{1,2}:\\d{2}\\s*(AM|PM))",
                Pattern.CASE_INSENSITIVE);
        Matcher matcher = patternDateTime.matcher(body);
        if (matcher.find()) {
            String datePart = matcher.group(1);
            String timePart = matcher.group(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy h:mm a", Locale.ENGLISH);
            timeStamp = LocalDateTime.parse(datePart + " " + timePart, formatter);
        }

        Pattern patternOperation = Pattern.compile("(?i)(Levantaste|Transferiste|Depositaste|Recebeste)");
        Matcher matcherOperation = patternOperation.matcher(body);
        if (matcherOperation.find()) {
            String operationLaweCase = matcherOperation.group(1).toLowerCase();
            if (operationLaweCase.contains("levantaste"))
                operation = "LEVANTAMENTO";
            else if (operationLaweCase.contains("transferiste"))
                operation = "TRANSFERENCIA";
            else if (operationLaweCase.contains("depositaste"))
                operation = "DEPOSITO";
            else if (operationLaweCase.contains("recebeste"))
                operation = "RECEPCAO";
        }

        switch (operation) {
            case "LEVANTAMENTO" -> {
                Pattern patternAmount = Pattern
                        .compile("(?i)(Levantaste|Transferiste|Depositaste|Recebeste)\\s([0-9.,]+)MT");
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String valor = matcherAmount.group(2).replace(",", ".");
                    amount = Double.valueOf(valor);
                }

                Pattern patternAgent = Pattern.compile("(?i)agente\\s+([0-9]+)\\s*-\\s*(.+?)(?=\\s+aos|\\.|$)");
                Matcher matcherAgent = patternAgent.matcher(body);
                if (matcherAgent.find()) {
                    account = matcherAgent.group(1);
                    name = matcherAgent.group(2).trim();
                }
                isReceived = false;

                Pattern patternTax = Pattern.compile("(?i)a taxa foi de\\s*([\\d.,]+)MT");
                Matcher matcherTax = patternTax.matcher(body);
                if (matcherTax.find()) {
                    String raw = matcherTax.group(1).replace(",", ".");
                    tax = Double.valueOf(raw);
                }

            }

            case "TRANSFERENCIA" -> {
                Pattern patternAmount = Pattern
                        .compile("(?i)(Levantaste|Transferiste|Depositaste|Recebeste)\\s([0-9.,]+)MT");
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String valor = matcherAmount.group(2).replace(",", ".");
                    amount = Double.valueOf(valor);
                }

                Pattern patternAccount = Pattern.compile(
                        "(?i)para\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ\\s]+?)(?=\\s+aos|\\s+\\d{1,2}/\\d{1,2}/\\d{2}|\\.)");
                Matcher matcherAccount = patternAccount.matcher(body);
                if (matcherAccount.find()) {
                    account = matcherAccount.group(1);
                    name = matcherAccount.group(2).trim();
                }
                isReceived = false;

               Pattern patternTax = Pattern.compile("(?i)a taxa foi de\\s*([\\d.,]+)MT");
Matcher matcherTax = patternTax.matcher(body);
if (matcherTax.find()) {
    String raw = matcherTax.group(1).replace(",", ".");
    tax = Double.valueOf(raw);
}


            }

            case "DEPOSITO" -> {
                Pattern patternAmount = Pattern.compile("(?i)valor de\\s([0-9.,]+)MT");
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String valor = matcherAmount.group(1).replace(",", "");
                    amount = Double.valueOf(valor);
                }

                Pattern patternAgent = Pattern
                        .compile("(?i)agente\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ0-9\\s\\-]+?)(?=\\s+aos|\\.|$)");
                Matcher matcherAgent = patternAgent.matcher(body);
                if (matcherAgent.find()) {
                    account = matcherAgent.group(1);
                    name = matcherAgent.group(2).trim();
                }
                isReceived = true;
                tax = 0.0;
            }

            case "RECEPCAO" -> {
                Pattern patternAmount = Pattern
                        .compile("(?i)(Levantaste|Transferiste|Depositaste|Recebeste)\\s([0-9.,]+)MT");
                Matcher matcherAmount = patternAmount.matcher(body);
                if (matcherAmount.find()) {
                    String valor = matcherAmount.group(2).replace(",", ".");
                    amount = Double.valueOf(valor);
                }

                Pattern patternAccount = Pattern.compile(
                        "(?i)(?:para|de)\\s+([0-9]+)\\s*-\\s*([A-Za-zÀ-ÿ\\s]+?)(?=\\s+aos|\\s+\\d{1,2}/\\d{1,2}/\\d{2}|\\.)");
                Matcher matcherAccount = patternAccount.matcher(body);
                if (matcherAccount.find()) {
                    account = matcherAccount.group(1);
                    name = matcherAccount.group(2).trim();
                }
                isReceived = true;
                tax = 0.0;
            }
        }

        return Sms.builder()
                .operatorName(operatorName)
                .sid(sid)
                .name(name)
                .account(account)
                .amount(amount)
                .tax(tax)
                .isReceived(isReceived)
                .date(TImeUtil.toEpochSeconds(timeStamp))
                .operation(OperationType.valueOf(operation))
                .build();
    }

}
