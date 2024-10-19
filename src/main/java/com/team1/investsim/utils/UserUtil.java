package com.team1.investsim.utils;

import com.team1.investsim.exceptions.InvalidEmailException;
import com.team1.investsim.exceptions.InvalidPasswordException;

import java.util.regex.Pattern;

public class UserUtil {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final int MIN_PASSWORD_LENGTH = 8;

    private static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static String validateEmail(String email) throws InvalidEmailException {
        if (!isValidEmail(email)) throw new InvalidEmailException("Email inválido");
        return email;
    }

    public static String validatePassword(String password) throws InvalidPasswordException {
        if (!isValidPassword(password)) throw new InvalidPasswordException("Senha inválida. Para ser válida é necessário ter pelo menos 8 dígitos, uma letra minúscula, uma letra maiúscula e um caractere especial");
        return password;
    }

    private static boolean isValidPassword(String password) {
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