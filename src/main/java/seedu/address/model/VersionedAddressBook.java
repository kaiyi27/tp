package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code AddressBook} that keeps track of its own history.
 * Adapted from AB4
 */
public class VersionedAddressBook extends AddressBook {
    /**
     * The list containing the states of the address book
     */
    private final List<ReadOnlyAddressBook> addressBookStateList;
    /**
     * The pointer to the current state in the state list
     */
    private int currentStatePointer;

    /**
     * Initialises the address book state list
     * @param initialState the pointer that points to the current state
     */
    public VersionedAddressBook(ReadOnlyAddressBook initialState) {
        super(initialState);

        addressBookStateList = new ArrayList<>();
        addressBookStateList.add(new AddressBook(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code AddressBook} state at the end of the state list
     * Undone states are removed from the state list
     * This means that executing commands after undoing a state will remove that state
     */
    public void commit() {
        removeStatesAfterCurrentPointer();
        addressBookStateList.add(new AddressBook(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        addressBookStateList.subList(currentStatePointer + 1, addressBookStateList.size()).clear();
    }

    /**
     * Restores the address book to its previous state
     * Shifts the state pointer back by 1 unless there is no more state to undo
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        ReadOnlyAddressBook prevState = addressBookStateList.get(currentStatePointer);
        resetData(prevState);
    }


    /**
     * Restores the address book to its previously undone state which only works
     * if commit has not been done after undoing
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(addressBookStateList.get(currentStatePointer));
    }

    /**
     * Replaces the current address book without increasing the state pointer
     */
    public void replaceCurrentAddressBook() {
        addressBookStateList.set(currentStatePointer, new AddressBook(this));
    }

    /**
     * Returns true if {@code undo()} has address book states to undo.
     */
    public boolean canUndo() {

        System.out.println("Current state pointer");
        System.out.println(currentStatePointer);

        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has address book states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < addressBookStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedAddressBook)) {
            return false;
        }

        VersionedAddressBook otherVersionedAddressBook = (VersionedAddressBook) other;

        // state check
        return super.equals(otherVersionedAddressBook)
                && addressBookStateList.equals(otherVersionedAddressBook.addressBookStateList)
                && currentStatePointer == otherVersionedAddressBook.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of addressBookState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of addressBookState list, unable to redo.");
        }
    }
}
