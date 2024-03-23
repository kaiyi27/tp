package seedu.address.logic.parser;


import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleMeetingCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Meeting;

/**
 * Parses input argument and creates a new Schedule Command object
 */
public class ScheduleCommandParser implements Parser<ScheduleMeetingCommand> {
    /**
     * Parses the user's date time input in string format
     * @param userInput of date time
     * @return Schedule Command with the index and meeting
     * @throws ParseException if invalid format
     */
    @Override
    public ScheduleMeetingCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_SCHEDULE);
        Index index;
        String meetingString;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleMeetingCommand.MESSAGE_USAGE), pe);
        }
        meetingString = argMultimap.getValue(PREFIX_SCHEDULE).orElse("");
        Meeting meeting;
        try {
            meeting = new Meeting(DateTimeUtil.stringToLocalDateTime(meetingString));
        } catch (ParseException e) {
            throw e;
        }
        return new ScheduleMeetingCommand(index, meeting);
    }
}
