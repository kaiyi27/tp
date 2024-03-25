package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;

import java.util.List;

import static seedu.address.logic.parser.CliSyntax.*;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MEETING_TIME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

public class CancelMeetingCommand extends Command{
    public static final String COMMAND_WORD = "cancel";

    public static final String MESSAGE_MEETING_CANCELLED_SUCCESS = "Meeting cancelled successfully: %1$s";
    public static final String MESSAGE_MEETING_INVALID_INDEX = "Invalid index provided for meeting list";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Cancels a meeting with the person identified "
            + "by the index number used in the last person listing. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_MEETING_INDEX + "[MEETING INDEX]"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_MEETING_INDEX + "1 ";
    Index personIndex;
    Index meetingIndex;
    public CancelMeetingCommand(Index personIndex, Index meetingIndex) {
        this.meetingIndex = meetingIndex;
        this.personIndex = personIndex;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person personToMeetOriginal = lastShownList.get(personIndex.getZeroBased());
        List<Meeting> meetings = personToMeetOriginal.getMeetings();
        if (meetings.size() <= (meetingIndex.getZeroBased())) {
            throw new CommandException(RescheduleMeetingCommand.MESSAGE_MEETING_INVALID_INDEX);
        }
        Person personToMeetUpdated = personToMeetOriginal.getCopy();
        try {
            personToMeetUpdated.cancelMeeting(meetingIndex.getZeroBased());
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
        model.setPerson(personToMeetOriginal, personToMeetUpdated);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        model.commitAddressBook();
        return new CommandResult(String.format(MESSAGE_MEETING_CANCELLED_SUCCESS,
                Messages.format(personToMeetUpdated)));
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ScheduleMeetingCommand)) {
            return false;
        }
        CancelMeetingCommand e = (CancelMeetingCommand) other;
        return personIndex.equals(e.personIndex)
                && meetingIndex.equals(e.meetingIndex);
    }
}
