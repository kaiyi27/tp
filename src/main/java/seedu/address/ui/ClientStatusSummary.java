package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import seedu.address.model.person.ClientStatusSummaryValues;

/**
 * A ui for the client status summary that is displayed above the person list panel.
 */
public class ClientStatusSummary extends UiPart<Region> {
    private static final String FXML = "ClientStatusSummary.fxml";

    @FXML
    private Label startSummary;
    @FXML
    private Label midSummary;
    @FXML
    private Label endSummary;

    public ClientStatusSummary(ClientStatusSummaryValues clientStatusSummaryValues) {
        super(FXML);
        startSummary.setText("Yet to start - "
                + clientStatusSummaryValues.NUMBER_OF_START
                + "/"
                + clientStatusSummaryValues.NUMBER_OF_CLIENTS);
        midSummary.setText("In progress - "
                + clientStatusSummaryValues.NUMBER_OF_MID
                + "/"
                + clientStatusSummaryValues.NUMBER_OF_CLIENTS);
        endSummary.setText("Completed - "
                + clientStatusSummaryValues.NUMBER_OF_END
                + "/"
                + clientStatusSummaryValues.NUMBER_OF_CLIENTS);
    }
}
