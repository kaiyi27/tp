package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_NO_PREFIXES_FOUND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_POLICY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.CombinedPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.PolicyContainsKeywordsPredicate;
import seedu.address.model.person.RelationshipContainsKeywordsPredicate;
import seedu.address.model.person.TagContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */


    @Override
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_RELATIONSHIP,
                PREFIX_TAG, PREFIX_POLICY);

        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        List<String> nameKeywords = argMultimap.getAllValues(PREFIX_NAME);
        List<String> relationshipKeywords = argMultimap.getAllValues(PREFIX_RELATIONSHIP);
        List<String> tagKeywords = argMultimap.getAllValues(PREFIX_TAG);
        List<String> policyKeywords = argMultimap.getAllValues(PREFIX_POLICY);

        // Validate nameKeywords list
        if (anyElementsContainWhitespaceOrBlank(nameKeywords)) {
            throw new ParseException("Name keywords cannot contain whitespace between words or be empty");
        }

        // Validate relationshipKeywords list
        if (anyElementsContainWhitespaceOrBlank(relationshipKeywords)) {
            throw new ParseException("Relationship keywords cannot contain whitespace between words or be empty");
        }

        // Validate tagKeywords list
        if (anyElementsContainWhitespaceOrBlank(tagKeywords)) {
            throw new ParseException("Tag keywords cannot contain whitespace between words or be empty");
        }

        // Validate policyKeywords list
        if (anyElementsContainWhitespaceOrBlank(policyKeywords)) {
            throw new ParseException("Policy keywords cannot contain whitespace between words or be empty");
        }

        if (nameKeywords.isEmpty() && relationshipKeywords.isEmpty()
                && tagKeywords.isEmpty() && policyKeywords.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_NO_PREFIXES_FOUND, FindCommand.MESSAGE_USAGE));
        }

        nameKeywords.stream().forEach(System.out::println);
        System.out.println((nameKeywords.isEmpty()));

        CombinedPredicate combinedPredicate =
                new CombinedPredicate(
                        new NameContainsKeywordsPredicate(nameKeywords),
                        new RelationshipContainsKeywordsPredicate(relationshipKeywords),
                        new TagContainsKeywordsPredicate(tagKeywords),
                        new PolicyContainsKeywordsPredicate(policyKeywords));
        return new FindCommand(combinedPredicate);
    }
    
    /**
     * Checks if any element in the given list contains whitespace or is blank.
     *
     * @param list the list of strings to be checked
     * @return {@code true} if any element contains whitespace or is blank, {@code false} otherwise
     * @throws NullPointerException if the specified list is null
     */
    public boolean anyElementsContainWhitespaceOrBlank(List<String> list) {
        for (String element : list) {
            if (element.isBlank() || element.contains(" ")) {
                return true;
            }
        }
        return false;
    }
}
