package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SCHEDULE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Meeting;
import seedu.address.model.person.Person;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Adds a meeting to a person in the address book
 */
public class ScheduleCommand extends Command{
    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a meeting with the person identified "
            + "by the index number used in the last person listing. "
            + "Existing meeting will be overwritten by the input.\n"
            + "Only able to have one meeting at a time "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_SCHEDULE + "[meeting time]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_SCHEDULE + "2025/06/06";
    private final Index index;
    private final Meeting meeting;
    public static final String MESSAGE_ADD_MEETING_SUCCESS = "Added meeting at %1$s to Person: %2$s";

    public ScheduleCommand(Index index, Meeting meeting) {
        requireAllNonNull(index, meeting);
        this.meeting = meeting;
        this.index = index;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToScheduleMeeting = lastShownList.get(index.getZeroBased());

        Person editedPerson = new Person(personToScheduleMeeting.getName(), personToScheduleMeeting.getPhone(),
                personToScheduleMeeting.getEmail(), personToScheduleMeeting.getAddress(),
                personToScheduleMeeting.getRelationship(), personToScheduleMeeting.getPolicy(),
                meeting,
                personToScheduleMeeting.getTags());

        model.setPerson(personToScheduleMeeting, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS); // might change to only show the person
        model.commitAddressBook();

        return new CommandResult(String.format(MESSAGE_ADD_MEETING_SUCCESS,
                meeting.displayString(), Messages.format(editedPerson)));
    }
    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ScheduleCommand)) {
            return false;
        }

        ScheduleCommand e = (ScheduleCommand) other;
        return index.equals(e.index)
                && meeting.equals(e.meeting);
    }
}
