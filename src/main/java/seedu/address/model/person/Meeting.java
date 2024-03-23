package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a Person's meeting in the address book.
 * Guarantees: immutable; is valid
 */
public class Meeting {
    public final LocalDateTime dateTime;

    /**
     * Constructs a {@code Meeting} with the person
     * @param dateTime a valid date and time
     */
    public Meeting(LocalDateTime dateTime) {
        requireNonNull(dateTime);
        this.dateTime = dateTime;
    }

    /**
     * Constructs a meeting using string format instead
     * @param dateTimeStorageString date time in string format
     */
    public Meeting(String dateTimeStorageString) {
        if (dateTimeStorageString.isEmpty()) {
            this.dateTime = null;
        } else {
            this.dateTime = LocalDateTime.parse(dateTimeStorageString);
        }

    }
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Meeting // instanceof handles nulls
                && dateTime.equals(((Meeting) other).dateTime)); // state check
    }

    /**
     * Returns ISO format for date time
     * Returns empty string if null
     * @return date time for meeting
     */
    @Override
    public String toString() {
        if (dateTime == null) {
            return "";
        }
        return dateTime.toString();
    }

    /**
     * Displays the date time in a suitable format
     * @return the string format of the date time
     */
    public String displayString() {
        if (dateTime == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm");
        if (dateTime.toLocalTime().equals(LocalTime.MIDNIGHT)) {
            formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        }

        return dateTime.format(formatter);
    }
}
