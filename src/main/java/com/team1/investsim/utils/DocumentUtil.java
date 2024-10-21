package com.team1.investsim.utils;

import com.team1.investsim.entities.types.UserType;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DocumentUtil {
    public static final int CNPJ_LENGTH = 14;
    public static final int CPF_LENGTH = 11;
    private static final String INVALID_CPF_PATTERN = "(\\d)\\1{10}";
    private static final String INVALID_CNPJ_PATTERN = "(\\d)\\1{13}";

    private static final Map<String, Boolean> documentValidationCache = new HashMap<>();

    public static Optional<String> validateAndClearDocument(String document) {
        if (!isValidDocument(document)) {
            return Optional.empty();
        }
        return Optional.of(clearDocument(document));
    }

    public static String clearDocument(String document) {
        if (document == null || document.isEmpty()) {
            return "";
        }
        return document.replaceAll("[^\\d]", "");
    }

    private static boolean isValidDocument(String document) {
        document = clearDocument(document);

        if (documentValidationCache.containsKey(document)) {
            return documentValidationCache.get(document);
        }

        boolean isValid = switch (document.length()) {
            case CPF_LENGTH -> isValidCPF(document);
            case CNPJ_LENGTH -> isValidCNPJ(document);
            default -> false;
        };

        documentValidationCache.put(document, isValid);

        return isValid;
    }

    private static boolean isValidCPF(String cpf) {
        if (cpf == null || cpf.matches(INVALID_CPF_PATTERN)) {
            return false;
        }

        int[] numbers = new int[11];
        for (int i = 0; i < 11; i++) {
            numbers[i] = Character.getNumericValue(cpf.charAt(i));
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += numbers[i] * (10 - i);
        }

        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) {
            firstDigit = 0;
        }

        if (numbers[9] != firstDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += numbers[i] * (11 - i);
        }

        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) {
            secondDigit = 0;
        }

        return numbers[10] == secondDigit;
    }

    private static boolean isValidCNPJ(String cnpj) {
        if (cnpj == null || cnpj.matches(INVALID_CNPJ_PATTERN)) {
            return false;
        }

        int[] numbers = new int[14];
        for (int i = 0; i < 14; i++) {
            numbers[i] = Character.getNumericValue(cnpj.charAt(i));
        }

        int[] weight1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += numbers[i] * weight1[i];
        }

        int firstDigit = 11 - (sum % 11);
        if (firstDigit > 9) {
            firstDigit = 0;
        }

        if (numbers[12] != firstDigit) {
            return false;
        }

        int[] weight2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        sum = 0;
        for (int i = 0; i < 13; i++) {
            sum += numbers[i] * weight2[i];
        }

        int secondDigit = 11 - (sum % 11);
        if (secondDigit > 9) {
            secondDigit = 0;
        }

        return numbers[13] == secondDigit;
    }

    public static Optional<UserType> getTypeByDocument(String document) {
        String cleanDoc = clearDocument(document);
        if (cleanDoc.isEmpty()) {
            return Optional.empty();
        }

        return switch (cleanDoc.length()) {
            case CPF_LENGTH -> Optional.of(UserType.FISICA);
            case CNPJ_LENGTH -> Optional.of(UserType.JURIDICA);
            default -> Optional.empty();
        };
    }
}