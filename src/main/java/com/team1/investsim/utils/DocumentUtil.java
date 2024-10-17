package com.team1.investsim.utils;

public class DocumentUtil {
    public static String cleanDocument(String document) {
        if (document == null || document.isEmpty()) {
            return "";
        }
        return document.replaceAll("[^\\d]", "");
    }

    public static boolean isValidDocument(String document) {
        document = cleanDocument(document);

        if (document.length() == 11) {
            return isValidCPF(document);
        }

        if (document.length() == 14) {
            return isValidCNPJ(document);
        }

        return false;
    }

    public static boolean isValidCPF(String cpf) {
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int sum = 0;
        int digit;
        for (int i = 0; i < 9; i++) {
            sum += (10 - i) * (cpf.charAt(i) - '0');
        }

        digit = 11 - (sum % 11);
        digit = (digit >= 10) ? 0 : digit;
        if (digit != (cpf.charAt(9) - '0')) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += (11 - i) * (cpf.charAt(i) - '0');
        }

        digit = 11 - (sum % 11);
        digit = (digit >= 10) ? 0 : digit;
        return digit == (cpf.charAt(10) - '0');
    }

    public static boolean isValidCNPJ(String cnpj) {
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] weights = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += (cnpj.charAt(i) - '0') * weights[i % 8];
        }

        int digit1 = 11 - (sum % 11);
        digit1 = (digit1 >= 10) ? 0 : digit1;
        if (digit1 != (cnpj.charAt(12) - '0')) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += (cnpj.charAt(i) - '0') * weights[i % 8];
        }

        int digit2 = 11 - (sum % 11);
        digit2 = (digit2 >= 10) ? 0 : digit2;
        return digit2 == (cnpj.charAt(13) - '0');
    }
}
