package pack.tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import pack.Main;

/**
 * Controller for TagHistoryUI.fxml.
 */
public class TagHistoryUIController {
    /**
     * History of tags for an image.
     */
    @FXML
    ListView<String> tagHistory;

    /**
     * Button to revert to a previous version of the name.
     */
    @FXML
    Button revert;

    /**
     * Initialize the list of previous names.
     */
    @FXML
    private void initialize() {
        ObservableList<String> history = FXCollections.observableArrayList(
                Main.currentImageInfo.getImage().getRenamingList());
        tagHistory.setItems(history);
    }

    /**
     * Revert the file name when the button revert is clicked.
     */
    @FXML
    private void handleRevert() {
        String toRevert = tagHistory.getSelectionModel().getSelectedItem();
        if (toRevert != null) {
            Main.currentImageInfo.getImage().revert(toRevert);
            Main.currentImageInfo.updateFileInfo();
            initialize();
        }

    }
}
