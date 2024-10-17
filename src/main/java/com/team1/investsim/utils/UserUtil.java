package com.team1.investsim.utils;

import java.util.regex.Pattern;

public class UserUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final int MIN_PASSWORD_LENGTH = 8;

    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            return false;
        }

        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasSpecialChar = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (isSpecialCharacter(ch)) {
                hasSpecialChar = true;
            }

            if (hasUppercase && hasLowercase && hasSpecialChar) {
                return true;
            }
        }

        return false;
    }

    private static boolean isSpecialCharacter(char ch) {
        String specialChars = "!@#$%^&*()_+-=[]{}|;:,.<>?";
        return specialChars.indexOf(ch) != -1;
    }
}