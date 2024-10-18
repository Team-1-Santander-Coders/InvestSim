package com.team1.investsim.utils;

import com.team1.investsim.exceptions.IllegalDateException;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DateUtil {
    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_WITHOUT_HOUR_PATTERN = "dd/MM/yyyy";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    private static final double DAY_DURATION = ChronoUnit.DAYS.getDuration().getSeconds();

    public static String dateToString(LocalDateTime date) {
        return date.format(DEFAULT_FORMATTER);
    }

    public static LocalDateTime stringToDate(String dateStr, String datePattern) throws IllegalDateException {
        if (!isValidDate(dateStr, datePattern)) {
            throw new IllegalDateException("Data inválida: " + dateStr + ". Verifique se o formato está correto e se a data é válida.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

        if (datePattern.equals(ISO8601_DATE_PATTERN) || datePattern.equals(DATE_WITHOUT_HOUR_PATTERN)) {
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.atStartOfDay();
        }

        return LocalDateTime.parse(dateStr, formatter);
    }

    public static boolean isValidDate(String dateStr, String datePattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);

            if (datePattern.equals(ISO8601_DATE_PATTERN) || datePattern.equals(DATE_WITHOUT_HOUR_PATTERN)) {
                LocalDate date = LocalDate.parse(dateStr, formatter);
                return date.getMonthValue() != 2 || date.getDayOfMonth() != 29 || date.isLeapYear();
            } else {
                LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                LocalDate date = dateTime.toLocalDate();
                return date.getMonthValue() != 2 || date.getDayOfMonth() != 29 || date.isLeapYear();
            }
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static List<LocalDateTime> getDaysBetweenDates(LocalDateTime startDate, LocalDateTime endDate) throws IllegalDateException {
        List<LocalDateTime> daysBetween = new ArrayList<>();

        if (startDate.isAfter(endDate)) {
            throw new IllegalDateException("A data de início deve ser anterior à data de término.");
        }

        LocalDate start = startDate.toLocalDate();
        LocalDate end = endDate.toLocalDate();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            daysBetween.add(date.atStartOfDay());
        }

        return daysBetween;
    }

    public static LocalDateTime getStartOfDay(LocalDateTime date) {
        return date.with(LocalTime.MIN);
    }

    public static boolean validateDifferenceBetweenDate(LocalDateTime startDate, LocalDateTime endDate) {
        return endDate.isBefore(startDate);
    }

    public static double calculateDifferenceBetweenDate(LocalDateTime startDate, LocalDateTime endDate) throws IllegalDateException {
        if (!validateDifferenceBetweenDate(startDate, endDate)) throw new IllegalDateException("Data de início não pode ser menor que data de fim.");
        long diffInSeconds = Duration.between(startDate, endDate).getSeconds();
        return diffInSeconds / DAY_DURATION ;
    }
}
