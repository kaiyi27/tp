package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_AGENDA;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_DURATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_NOTES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.logic.parser.ParserUtil.getLocalDateTimeFromDayOfWeek;
import static seedu.address.logic.parser.ParserUtil.isDayOfWeek;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.ScheduleMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Meeting;

/**
 * Parses input arguments and creates a new {@code ScheduleMeetingCommand} object
 */
public class ScheduleMeetingCommandParser implements Parser<ScheduleMeetingCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code ScheduleMeetingCommand}
     * and returns a {@code ScheduleMeetingCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public ScheduleMeetingCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_MEETING_DATE, PREFIX_MEETING_TIME, PREFIX_MEETING_DURATION,
                        PREFIX_MEETING_AGENDA, PREFIX_MEETING_NOTES);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ScheduleMeetingCommand.MESSAGE_USAGE), ive);
        }

        if (!argMultimap.arePrefixesPresent(PREFIX_MEETING_DATE, PREFIX_MEETING_TIME, PREFIX_MEETING_DURATION,
                PREFIX_MEETING_AGENDA)) {
            throw new ParseException("Date, time, duration, and agenda are required for scheduling a meeting.");
        }

        LocalDate meetingDate;
        LocalTime meetingTime;
        Duration duration;
        try {
            String stringMeetingDate = argMultimap.getValue(PREFIX_MEETING_DATE).orElse("");
            String stringMeetingTime = argMultimap.getValue(PREFIX_MEETING_TIME).orElse("");
            String stringDateAndTime = stringMeetingDate + " " + stringMeetingTime;
            if (isDayOfWeek(stringDateAndTime)) {
                LocalDateTime datetime = getLocalDateTimeFromDayOfWeek(stringDateAndTime);
                meetingDate = datetime.toLocalDate();
                meetingTime = datetime.toLocalTime();
            } else {
                meetingDate = ParserUtil.parseDate(stringMeetingDate);
                meetingTime = ParserUtil.parseTime(stringMeetingTime);
            }
            duration = ParserUtil.parseDuration(argMultimap.getValue(PREFIX_MEETING_DURATION).orElse(""));
        } catch (ParseException e) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    ScheduleMeetingCommand.MESSAGE_USAGE), e);
        }

        String agenda = argMultimap.getValue(PREFIX_MEETING_AGENDA)
                .orElseThrow(() -> new ParseException("Agenda is required."));
        String notes = argMultimap.getValue(PREFIX_MEETING_NOTES).orElse("");

        Meeting meeting = new Meeting(meetingDate, meetingTime, duration, agenda, notes);

        return new ScheduleMeetingCommand(index, meeting);
    }
}
