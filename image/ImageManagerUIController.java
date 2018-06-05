package pack.image;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import pack.Main;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Controller for ImageManagerUI.fxml.
 */
public class ImageManagerUIController {

    /**
     * Moves Images.
     */
    @FXML
    MenuItem moveImage;

    /**
     * Manages Tags.
     */
    @FXML
    MenuItem manageTag;

    /**
     * The Tag History of the Image shown.
     */
    @FXML
    MenuItem tagHistory;

    /**
     * The picture displayed.
     */
    @FXML
    ImageView preview;

    /**
     * The path of the image shown.
     */
    @FXML
    Text imagePath;

    /**
     * The back button.
     */
    @FXML
    Button backButton;

    /**
     * The TextField display current directory.
     */
    @FXML
    TextField directoryTextField;

    /**
     * The TreeTable to display files, refer to TreeTableView class.
     */
    @FXML
    TreeTableView<FileInfo> treeTableView;

    /**
     * The first column display the directories and images.
     */
    @FXML
    TreeTableColumn<FileInfo, String> imageColumn;

    /**
     * The second column display the tags.
     */
    @FXML
    TreeTableColumn<FileInfo, String> tagColumn;

    /**
     * The pop-up Menu on Tag column.
     */
    @FXML
    ContextMenu tagMenu;

    /**
     * The first option of tagMenu to manage tag.
     */
    @FXML
    MenuItem addTagC;

    /**
     * The second option to see the tag history.
     */
    @FXML
    MenuItem tagHistoryC;

    /**
     * The pop-up Menu on Image column.
     */
    @FXML
    ContextMenu imageMenu;

    /**
     * The option of imageMenu to move selected image.
     */
    @FXML
    MenuItem moveImageC;

    /**
     * The option of Log to see log of all renaming history.
     */
    @FXML
    MenuItem renameLog;

    /**
     * The root of the TreeTableView.
     */
    private TreeItem<FileInfo> rootItem;

    /**
     * Initializes the tables of the UI.
     */
    @FXML
    private void initialize() {
        // modify TreeTableView
        updateTree();

        // modify TextField
        updateTextField();
    }

    /**
     * EventHandler: when the user clicks the option, change the view mode to show all images in and
     * under the current directory.
     */
    @FXML
    private void handleViewModeInShowAllImages() {
            ImageManager.setViewAllImages(true);
            updateTree();
    }

    /**
     * EventHandler: when the use clicks the option, change the view mode to show images and sub
     * directories in tree structure.
     */
    @FXML
    private void handleViewModeInTreeStructure() {
        ImageManager.setViewAllImages(false);
        updateTree();
    }

    /**
     * EventHandler: when TextField has the focus and Enter key pressed, update the TextField or reset it.
     *
     * @param e event being handled
     */
    @FXML
    private void handleEnterTextField(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            updateTextField();
        }
    }

    /**
     * EventHandler: when Back Button is clicked. Set the current directory to the parent and update
     * the TextField and TreeTableView.
     */
    @FXML
    private void handleBackButton() {
        String parentPathname = ImageManager.getDirectory().getParent();
        ImageManager.setDirectory(new File(parentPathname));
        directoryTextField.setText(parentPathname);
        updateTree();
    }

    /**
     * EventHandler: when Back Button is clicked. Set the current directory to the parent and update
     * the TextField and TreeTableView.
     */
    @FXML
    private void handleChooseButton() {
        Stage Popup = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(ImageManager.getDirectory());
        File newDirectory = directoryChooser.showDialog(Popup);
        if (newDirectory != null) {
            ImageManager.setDirectory(newDirectory);
            updateTree();
            updateTextField();
        }
    }

    /**
     * EventHandler: when the add tag button in context menu is pressed. Pop up the second stage.
     *
     * @throws IOException if it can't access TagManagerUI.fxml
     */
    @FXML
    private void handleManageTag() throws IOException {
        Stage Popup = new Stage();
        Popup.setTitle("Tag Manager");
        VBox root =
                FXMLLoader.load(
                        getClass()
                                .getResource(".." + File.separator
                                        + "tag" + File.separator
                                        + "TagManagerUI.fxml"));
        Popup.setScene(new Scene(root));
        Popup.show();
    }

    /**
     * EventHandler: when the TagHistory in tagMenu is pressed. Pop up TagHistoryUI.
     *
     * @throws IOException if it can't access TagHistoryUI.fxml
     */
    @FXML
    private void handleTagHistory() throws IOException {
        Stage Popup = new Stage();
        Popup.setTitle("Name History");
        VBox root =
                FXMLLoader.load(
                        getClass()
                                .getResource(".." + File.separator
                                        + "tag" + File.separator
                                        + "TagHistoryUI.fxml"));
        Popup.setScene(new Scene(root));
        Popup.show();
    }

    /**
     * EventHandler: when rename log in log menu is pressed.
     *
     * @throws IOException if it can't access ImageLog.log
     */
    @FXML
    private void handleRenameHistory() throws IOException {
        File logFile = new File("ImageLog.log");
        if (logFile.exists()) {
            Desktop.getDesktop().open(logFile);
        }
    }

    /**
     * EventHandler: when the moveImageC in imageMenu is pressed.
     */
    @FXML
    private void handleMoveImage() {
        pack.image.Image selectedImage = Main.currentImageInfo.getImage();
        Stage Popup = new Stage();
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(selectedImage.getFile().getParentFile());
        File newDirectory = directoryChooser.showDialog(Popup);
        if (newDirectory != null) {
            ImageManager.moveImage(selectedImage, newDirectory);
            Main.currentImageInfo.updateFileInfo();
            updateTree();
        }
    }


    /**
     * Update the TextField of current directory.
     */
    private void updateTextField() {
        directoryTextField.setText(ImageManager.getDirectory().getPath());
    }

    /**
     * Update the TreeTableView of current directory.
     */
    private void updateTree() {
        // get root
        rootItem = getTree(rootItem);
        imageColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FileInfo, String> param) ->
                        param.getValue().getValue().nameProperty());
        imageColumn.setCellFactory(param -> new customImageTTC());
        tagColumn.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<FileInfo, String> param) ->
                        param.getValue().getValue().tagsProperty());
        tagColumn.setCellFactory(param -> new customTagTTC());
        treeTableView.setRoot(rootItem);
        treeTableView.setEditable(true);
        treeTableView.setShowRoot(false);
        treeTableView.getColumns().setAll(imageColumn, tagColumn);
        treeTableView
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {
                            if (newValue != null && newValue.getValue().isImage()) {
                                Main.currentImageInfo = newValue.getValue();
                                manageTag.setText("Manage Tag for this image");
                                moveImage.setDisable(false);
                                tagHistory.setDisable(false);
                            } else {
                                Main.currentImageInfo = null;
                                manageTag.setText("Manage Tag Library");
                                moveImage.setDisable(true);
                                tagHistory.setDisable(true);
                            }
                        });
    }

    /**
     * Helper in updateTree. Get the root node of TreeTableView of the current directory.
     *
     * @param treeItem the tree to be updated while traversing through it
     */
    private TreeItem<FileInfo> getTree(TreeItem<FileInfo> treeItem) {
        if (treeItem == null)
            // create the root note of this tree if null
            treeItem = new TreeItem<>(new FileInfo(ImageManager.getDirectory()));
        treeItem.getChildren().clear();
        // add the subdirectories in form of FileInfo
        for (File subdirectory : ImageManager.getSubDirectories()) {
            TreeItem<FileInfo> subItem = new TreeItem<>(new FileInfo(subdirectory));
            treeItem.getChildren().add(subItem);
        }
        // add the Images in form of FileInfo
        for (Image image : ImageManager.getImages()) {
            TreeItem<FileInfo> subItem = new TreeItem<>(new FileInfo(image));
            treeItem.getChildren().add(subItem);
        }
        return treeItem;
    }

    private void handleCellClicked(
            TreeTableCell<FileInfo, String> ttc, MouseEvent event, ContextMenu cm) {
        // handle double click
        if (event.getClickCount() == 2 && !ttc.isEmpty()) {
            FileInfo fileInfo = (ttc.getTreeTableRow().getTreeItem().getValue());
            // directory
            if (!fileInfo.isImage()) {
                // set the new root and update the TreeTableView
                ImageManager.setDirectory(fileInfo.getFile());
                updateTree();
                directoryTextField.setText(ImageManager.getDirectory().getPath());
                // image
            } else {
                // preview the image
                preview.setImage(
                        new javafx.scene.image.Image(Main.currentImageInfo.getFile().toURI().toString()));
                imagePath.setText(Main.currentImageInfo.getImage().getFile().getPath());
            }
            // handle right click
        } else if (event.getButton() == MouseButton.SECONDARY && !ttc.isEmpty()) {
            FileInfo fileInfo = (ttc.getTreeTableRow().getTreeItem().getValue());
            if (fileInfo.isImage()) {
                ttc.setContextMenu(cm);
            }
        }
    }

    /**
     * Custom TreeTableCell for the image column to add event for a TreeTableItem.
     */
    public class customImageTTC extends TreeTableCell<FileInfo, String> {

        private customImageTTC() {
            super();
            setOnMouseClicked(event -> handleCellClicked(this, event, imageMenu));
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            // refresh the cell if the item has been removed
            if (item == null || empty) {
                setText(null);
            } else {
                // refresh the cell by the current TreeTableViewItem
                setText(item);
                TreeItem<FileInfo> treeItem = this.getTreeTableRow().getTreeItem();
                if (treeItem != null && !treeItem.getValue().isImage())
                    setGraphic(
                            new ImageView(
                                    new javafx.scene.image.Image(
                                            getClass()
                                                    .getResourceAsStream(
                                                            ".."
                                                                    + File.separator
                                                                    + "UIelements"
                                                                    + File.separator
                                                                    + "directory.png"))));
            }
        }
    }

    /**
     * Custom TreeTableCell for the tag column to add event for a TreeTableItem.
     */
    public class customTagTTC extends TreeTableCell<FileInfo, String> {

        private customTagTTC() {
            super();
            setOnMouseClicked(
                    event -> {
                        handleCellClicked(this, event, tagMenu);
                    });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            // refresh the cell if the item has been removed
            if (item == null || empty) {
                setText(null);
            } else {
                // refresh the cell by the current TreeTableViewItem
                setText(item);
            }
        }
    }
}
