package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;

/**
 * A ui for the client status summary that is displayed above the person list panel.
 */
public class ClientStatusSummary extends UiPart<Region> {
    private static final String FXML = "ClientStatusSummary.fxml";

    @FXML
    private TextArea clientStatusSummary1;

    public ClientStatusSummary() {
        super(FXML);
    }

    public void updateClientStatusSummary() {
        clientStatusSummary1.setText("client status summary");
    }
}
