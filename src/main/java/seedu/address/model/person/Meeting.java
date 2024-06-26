package seedu.address.model.person;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Represents a meeting with a specific date, time, duration, agenda, and optional notes.
 */
public class Meeting {

    public static final String MESSAGE_CONSTRAINTS_DATE = "Meeting dates should be present and should "
            + "in the one of the following formats: YYYY-MM-DD, "
            + "yyyy-MM-dd, dd-MM-yyyy, yyyyMMdd, yyyy MM dd, dd MM yyyy, ddMMyyyy .";

    public static final String MESSAGE_CONSTRAINTS_TIME = "Meeting times should be in the format HH:MM.";

    public static final String MESSAGE_CONSTRAINTS_DURATION = "Meeting duration should be a "
            +
            "positive integer representing minutes.";

    public static final String MESSAGE_CONSTRAINTS_AGENDA = "Meeting agenda should not be blank.";
    public static final String MESSAGE_CONSTRAINTS_NOTES = "Meeting notes is optional and can be blank.";
    private LocalDate meetingDate;
    private LocalTime meetingTime;

    private LocalDateTime startDateTime; // New field
    private Duration duration;
    private String agenda;
    private String notes; //make it optional

    /**
     * Constructs a new Meeting object with the specified details.
     *
     * @param meetingDate   The date of the meeting.
     * @param meetingTime   The time of the meeting.
     * @param duration      The duration of the meeting.
     * @param agenda        The agenda of the meeting.
     * @param notes         Optional notes for the meeting.
     */
    public Meeting(LocalDate meetingDate, LocalTime meetingTime, Duration duration, String agenda, String notes) {
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.duration = duration;
        this.agenda = agenda;
        this.notes = notes;
        this.startDateTime = LocalDateTime.of(meetingDate, meetingTime); // Initialize new field
    }


    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getAgenda() {
        return agenda;
    }

    public void setAgenda(String agenda) {
        this.agenda = agenda;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    /**
     * Checks if this meeting has already occurred.
     *
     * @return True if the meeting is in the past, false otherwise.
     */
    public boolean isExpired() {
        return LocalDateTime.of(meetingDate, meetingTime).isBefore(LocalDateTime.now());
    }


    @Override
    public int hashCode() {
        return Objects.hash(meetingDate, meetingTime, duration, agenda, notes);
    }

    @Override
    public String toString() {
        return "Meeting{"
                + "meetingDate=" + meetingDate
                + ", meetingTime=" + meetingTime
                + ", duration=" + duration
                + ", agenda='" + agenda + '\''
                + ", notes='" + notes + '\''
                + '}';
    }

    // In Meeting class
    /**
     * Checks if this meeting overlaps with another meeting.
     *
     * @param other The other meeting to check for overlap.
     * @return True if there is an overlap, false otherwise.
     */
    public boolean overlapsWith(Meeting other) {
        LocalDateTime start = this.startDateTime;
        LocalDateTime end = start.plus(this.getDuration());
        LocalDateTime otherStart = LocalDateTime.of(other.getMeetingDate(), other.getMeetingTime());
        LocalDateTime otherEnd = otherStart.plus(other.getDuration());

        return start.isBefore(otherEnd) && otherStart.isBefore(end);
    }

    public boolean isComingUp() {
        return LocalDateTime.now().plusWeeks(2).isAfter(this.startDateTime);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Meeting meeting = (Meeting) obj;
        return Objects.equals(meetingDate, meeting.meetingDate)
                && Objects.equals(meetingTime, meeting.meetingTime)
                && Objects.equals(duration, meeting.duration)
                && Objects.equals(agenda, meeting.agenda)
                && Objects.equals(notes, meeting.notes);
    }
}
