package seedu.address.model.person;

/**
 * Represents the summary values of clients' status in the person list.
 */
public class ClientStatusSummaryValues {
    public final int numberOfClients;
    public final int numberOfStart;
    public final int numberOfMid;
    public final int numberOfEnd;

    /**
     * Creates a {@code ClientStatusSummaryValues} with the given values.
     */
    public ClientStatusSummaryValues(int numClients, int numStart, int numMid, int numEnd) {
        numberOfClients = numClients;
        numberOfStart = numStart;
        numberOfMid = numMid;
        numberOfEnd = numEnd;
    }
}
