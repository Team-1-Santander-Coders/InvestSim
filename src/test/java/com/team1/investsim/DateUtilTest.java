package com.team1.investsim;

import com.team1.investsim.utils.DateUtil;
import com.team1.investsim.exceptions.IllegalDateException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class DateUtilTest {

    @Test
    public void testValidDateWithTime() throws IllegalDateException {
        String validDateStr = "15/10/2024 15:30:45";
        String pattern = DateUtil.DEFAULT_DATE_PATTERN;

        assertTrue(DateUtil.isValidDate(validDateStr, pattern), "A data deveria ser válida");

        LocalDateTime result = DateUtil.stringToDate(validDateStr, pattern);
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 10, 15, 15, 30, 45));
    }

    @Test
    public void testValidDateWithoutTime() throws IllegalDateException {
        String validDateStr = "2024-10-15";
        String pattern = DateUtil.CSV_DATE_PATTERN;

        assertTrue(DateUtil.isValidDate(validDateStr, pattern), "A data deveria ser válida");

        LocalDateTime result = DateUtil.stringToDate(validDateStr, pattern);
        assertThat(result).isEqualTo(LocalDate.of(2024, 10, 15).atStartOfDay());
    }

    @Test
    public void testInvalidDateLeapYear() {
        String invalidDateStr = "29/02/2021";
        String pattern = DateUtil.DEFAULT_DATE_PATTERN;

        assertFalse(DateUtil.isValidDate(invalidDateStr, pattern), "A data não deveria ser válida");

        IllegalDateException exception = assertThrows(IllegalDateException.class, () -> {
            DateUtil.stringToDate(invalidDateStr, pattern);
        });

        assertThat(exception.getMessage()).contains("Data inválida");
    }

    @Test
    public void testValidDateLeapYear() throws IllegalDateException {
        String validDateStr = "29/02/2024 00:00:00";
        String pattern = DateUtil.DEFAULT_DATE_PATTERN;

        assertTrue(DateUtil.isValidDate(validDateStr, pattern), "A data deveria ser válida");

        LocalDateTime result = DateUtil.stringToDate(validDateStr, pattern);
        assertThat(result).isEqualTo(LocalDate.of(2024, 2, 29).atStartOfDay());
    }

    @Test
    public void testInvalidDateFormat() {
        String invalidDateStr = "15-10-2024 15:30:45";
        String pattern = DateUtil.DEFAULT_DATE_PATTERN;

        assertFalse(DateUtil.isValidDate(invalidDateStr, pattern), "A data com formato inválido não deveria ser válida");

        IllegalDateException exception = assertThrows(IllegalDateException.class, () -> {
            DateUtil.stringToDate(invalidDateStr, pattern);
        });

        assertThat(exception.getMessage()).contains("Data inválida");
    }

    @Test
    public void testDateWithoutTimeCompletesZeros() throws IllegalDateException {
        String validDateStr = "2024-10-15";
        String pattern = DateUtil.CSV_DATE_PATTERN;

        LocalDateTime result = DateUtil.stringToDate(validDateStr, pattern);

        assertThat(result.getHour()).isEqualTo(0);
        assertThat(result.getMinute()).isEqualTo(0);
        assertThat(result.getSecond()).isEqualTo(0);
    }

    @Test
    public void testValidateDifferenceBetweenDate() {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 15, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 10, 16, 12, 0);

        assertTrue(DateUtil.validateDifferenceBetweenDate(startDate, endDate),
                "A data inicial deveria ser anterior à data final");

        LocalDateTime invalidEndDate = LocalDateTime.of(2024, 10, 14, 12, 0);
        assertFalse(DateUtil.validateDifferenceBetweenDate(startDate, invalidEndDate),
                "A data inicial não deveria ser anterior à data final");
    }

    @Test
    public void testCalculateDifferenceBetweenDate() throws IllegalDateException {
        LocalDateTime startDate = LocalDateTime.of(2024, 10, 15, 12, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 10, 16, 12, 0);

        double difference = DateUtil.calculateDifferenceBetweenDate(startDate, endDate);
        assertEquals(1.0, difference, 0.001, "A diferença deveria ser de 1 dia");

        LocalDateTime endDateWithHours = LocalDateTime.of(2024, 10, 16, 12, 30);
        double differenceWithHours = DateUtil.calculateDifferenceBetweenDate(startDate, endDateWithHours);
        assertEquals(1.0208, differenceWithHours, 0.001, "A diferença deveria ser de cerca de 1.0208 dias");
    }
}
