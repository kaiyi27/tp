package seedu.address.logic.parser;

import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
public class ScheduleMeetingParserTest {

    private ScheduleMeetingCommandParser scheduleMeetingCommandParser = new ScheduleMeetingCommandParser();
    @Test
    public void execute_meetingInPast_throwsParseException() {
        String pastMeetingString = "1 md/29 05 2021 mt/00:00 mdur/60 ma/hi";
        assertThrows(ParseException.class, "Cannot schedule a meeting in the past.", () ->
                scheduleMeetingCommandParser.parse(pastMeetingString));
    }

    @Test
    public void execute_meetingInDistantFuture_throwsParseException() {
        String meetingInDistantFutureString = "2 md/29 05 2026 mt/00:00 mdur/60 ma/hi";
        assertThrows(ParseException.class, "Cannot schedule a meeting more than a year in the future.", () ->
                scheduleMeetingCommandParser.parse(meetingInDistantFutureString));
    }

}
