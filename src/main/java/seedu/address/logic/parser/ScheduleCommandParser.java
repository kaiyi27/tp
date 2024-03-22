package seedu.address.logic.parser;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScheduleCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Meeting;

import java.time.LocalDateTime;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;

public class ScheduleCommandParser implements Parser<ScheduleCommand> {
    @Override
    public ScheduleCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(userInput, PREFIX_SCHEDULE);
        Index index;
        String meetingString;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScheduleCommand.MESSAGE_USAGE), pe);
        }
        meetingString = argMultimap.getValue(PREFIX_SCHEDULE).orElse("");
        Meeting meeting;
        try{
            meeting = new Meeting(DateTimeUtil.stringToLocalDateTime(meetingString));
        } catch(ParseException e) {
            throw e;
        }
        return new ScheduleCommand(index, meeting);
    }
}
