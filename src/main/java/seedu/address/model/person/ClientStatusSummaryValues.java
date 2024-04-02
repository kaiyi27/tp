package seedu.address.model.person;

/**
 * Represents the summary values of clients' status in the person list.
 */
public class ClientStatusSummaryValues {
    public final int NUMBER_OF_CLIENTS;
    public final int NUMBER_OF_START;
    public final int NUMBER_OF_MID;
    public final int NUMBER_OF_END;

    public ClientStatusSummaryValues(int numClients, int numStart, int numMid, int numEnd) {
        NUMBER_OF_CLIENTS = numClients;
        NUMBER_OF_START = numStart;
        NUMBER_OF_MID = numMid;
        NUMBER_OF_END = numEnd;
    }
}
