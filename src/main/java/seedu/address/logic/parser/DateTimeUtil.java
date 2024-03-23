package seedu.address.logic.parser;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains utility methods to parse valid LocalDateTime format
 */
public class DateTimeUtil {
    public static final String[] POSSIBLE_DAY_OF_WEEK_FORMATS = {"EEEE", "EEE", "EEEEE"};
    public static final String[] POSSIBLE_DATETIME_FORMATS = {
        "yyyy-MM-dd HH:mm",
        "dd-MM-yyyy HH:mm",
        "yyyyMMdd HH:mm",
        "yyyy MM dd HH:mm",
        "dd MM yyyy HH:mm",
        "ddMMyyyy HH:mm"
        // Add more formats as needed
    };

    /**
     * Converts valid String format to LocalDateTime
     * @param inputString date time string
     * @return LocalDateTime format of string
     * @throws ParseException if invalid format of string
     */
    public static LocalDateTime stringToLocalDateTime(String inputString) throws ParseException {
        try {
            String dateTimeString = addsTimeIfNonexistent(inputString);
            if (isDayOfWeek(dateTimeString)) {
                return getLocalDateTimeFromDayOfWeek(dateTimeString);
            }
            LocalDateTime dateTime = getLocalDateTimeFromPossibleFormats(dateTimeString);
            return dateTime;
        } catch (ParseException e) {
            throw e;
        }
    }
    private static LocalDateTime getLocalDateTimeFromDayOfWeek(String dateString) throws ParseException {
        String[] splitdateString;
        LocalTime chosenTime;
        try {
            splitdateString = dateString.split("\\s+");
            chosenTime = LocalTime.parse(splitdateString[1], DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new ParseException("Invalid time, use the format of \"day HH:mm\"");
        }
        for (String format : POSSIBLE_DAY_OF_WEEK_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                DayOfWeek dayOfWeek = DayOfWeek.from(formatter.parse(splitdateString[0]));
                LocalDate currentDate = LocalDate.now();
                LocalDate currentDay = currentDate.with(dayOfWeek);
                LocalDate nextOccurrence = findNextDayOfWeek(currentDate, dayOfWeek);
                nextOccurrence = checkIfTimeHasPassedOnSameDayAsCurrent(chosenTime, dayOfWeek,
                        currentDay, nextOccurrence);
                LocalDateTime dateTime = nextOccurrence.atTime(chosenTime);
                return dateTime;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }
        throw new ParseException("Invalid format for day of week");
    }
    private static LocalDate checkIfTimeHasPassedOnSameDayAsCurrent(LocalTime time, DayOfWeek dayOfWeek,
                                                                    LocalDate currentDay, LocalDate nextOccurrence) {
        if (nextOccurrence.with(dayOfWeek) == currentDay) {
            LocalTime currentTime = LocalTime.now();
            if (time.isBefore(currentTime)) {
                nextOccurrence = nextOccurrence.plusWeeks(1);
            }
        }
        return nextOccurrence;
    }

    private static LocalDate findNextDayOfWeek(LocalDate currentDate, DayOfWeek targetDayOfWeek) {
        return currentDate.with(TemporalAdjusters.nextOrSame(targetDayOfWeek));
    }
    private static LocalDateTime getLocalDateTimeFromPossibleFormats(String dateString) throws ParseException {
        for (String format : POSSIBLE_DATETIME_FORMATS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern(format));
                return dateTime;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }
        throw new ParseException("enter date and time using yyyy MM dd HH:mm");
    }
    private static String addsTimeIfNonexistent(String dateString) {
        if (!dateString.contains(":")) { // might consider using localtime to find time
            dateString = dateString + " 00:00";
        }
        return dateString;
    }
    private static boolean isDayOfWeek(String dateTime) {
        String date = dateTime.split("\\s+")[0];
        for (String format : POSSIBLE_DAY_OF_WEEK_FORMATS) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                DayOfWeek.from(formatter.parse(date));
                return true;
            } catch (Exception ignored) {
                // If parsing fails, ignore the exception and try the next format
            }
        }
        return false;
    }



}
