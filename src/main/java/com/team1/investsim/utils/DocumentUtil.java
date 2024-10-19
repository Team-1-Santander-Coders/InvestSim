package com.team1.investsim.utils;

import com.team1.investsim.entities.types.UserType;
import com.team1.investsim.exceptions.InvalidDocumentException;

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
        if (cpf.matches(INVALID_CPF_PATTERN)) {
            return false;
        }
        return checkDocumentDigits(cpf, CPF_LENGTH - 2, 9, 11, new int[]{10, 9, 8, 7, 6, 5, 4, 3, 2});
    }

    private static boolean isValidCNPJ(String cnpj) {
        if (cnpj.matches(INVALID_CNPJ_PATTERN)) {
            return false;
        }
        return checkDocumentDigits(cnpj, CNPJ_LENGTH - 2, 12, 13, new int[]{5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2});
    }

    private static boolean checkDocumentDigits(String document, int numDigits, int firstIndex, int lastIndex, int[] weights) {
        int firstDigit = calculateDigit(document, numDigits, weights);
        if (firstDigit != (document.charAt(firstIndex) - '0')) {
            return false;
        }

        int secondDigit = calculateDigit(document, numDigits + 1, weights);
        return secondDigit == (document.charAt(lastIndex) - '0');
    }

    private static int calculateDigit(String document, int numDigits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < numDigits; i++) {
            sum += (document.charAt(i) - '0') * weights[i % weights.length];
        }
        int digit = 11 - (sum % 11);
        return (digit >= 10) ? 0 : digit;
    }

    public static Optional<UserType> getTypeByDocument(String document) {
        int length = clearDocument(document).length();

        return switch (length) {
            case CPF_LENGTH -> Optional.of(UserType.FISICA);
            case CNPJ_LENGTH -> Optional.of(UserType.JURIDICA);
            default -> Optional.empty();
        };
    }
}
