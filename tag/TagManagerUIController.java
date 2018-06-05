package pack.tag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import pack.Main;
import pack.image.FileInfo;

import java.util.ArrayList;

public class TagManagerUIController {

    /**
     * TextField where a new tag gets typed in.
     */
    @FXML
    TextField newTag;

    /**
     * Button to add a new tag.
     */
    @FXML
    Button addNewTag;

    /**
     * Button to delete the tag from current Image that is selected.
     */
    @FXML
    Button deleteSelectedTag;

    /**
     * Button to add the tag from current Image that is selected.
     */
    @FXML
    Button addSelectedTag;

    /**
     * List of all tags assigned to Image.
     */
    @FXML
    ListView<TagInfo> assignedTags;

    /**
     * List of all tags available to Image.
     */
    @FXML
    ListView<TagInfo> tagLibrary;

    /**
     * Shows if there is currently an Image.
     */
    private boolean noImage = (Main.currentImageInfo == null);

    /**
     * Initialize the lists of tags.
     */
    @FXML
    private void initialize() {
        if (!noImage) {
            updateListView(assignedTags, Main.currentImageInfo.getImage().getAssignedTags());
            updateListView(tagLibrary, TagManager.getAvailableTags(Main.currentImageInfo.getImage()));
            showImageSpecificFunctions();
        } else {
            updateListView(tagLibrary, TagManager.getAllTags());
            assignedTags.setDisable(true);
            assignedTags.setMaxHeight(0);
            addSelectedTag.setDisable(true);
            addSelectedTag.setMaxHeight(0);
            hideImageSpecificFunctions();
        }
    }

    /**
     * Hides functionality only used for Images.
     */
    private void hideImageSpecificFunctions() {
        assignedTags.setVisible(false);
        deleteSelectedTag.setVisible(false);
        addSelectedTag.setVisible(false);
        tagLibrary.setMinHeight(400);
    }

    /**
     * Shows functionality only used for Images.
     */
    private void showImageSpecificFunctions() {
        assignedTags.setVisible(true);
        deleteSelectedTag.setVisible(true);
        addSelectedTag.setVisible(true);
        tagLibrary.setPrefHeight(200);
    }

    /**
     * Adds a new tag from what is typed by user.
     */
    @FXML
    private void handleAddNewTag() {
        String typedTag = newTag.getText();
        for (TagInfo existing : tagLibrary.getItems()) {
            if (typedTag.equals(existing.getName())) {
                if (!noImage) {
                    addTag(existing);
                }
                newTag.clear();
                return;
            }
        }
        Tag tag = TagManager.createTag(typedTag);
        TagInfo tagInfo = new TagInfo(tag.getName(), tag);
        if (!noImage) {
            addTag(tagInfo);
        } else {
            tagLibrary.getItems().add(tagInfo);
        }
        newTag.clear();
    }

    /**
     * Adds a new tag from what is typed by user.
     */
    @FXML
    private void handleCreateNewTag() {
        String typedTag = newTag.getText();
        for (TagInfo existing : tagLibrary.getItems()) {
            if (typedTag.equals(existing.getName())) {
                newTag.clear();
                return;
            }
        }
        Tag tag = TagManager.createTag(typedTag);
        TagInfo tagInfo = new TagInfo(tag.getName(), tag);
        tagLibrary.getItems().add(tagInfo);
        newTag.clear();
    }

    /**
     * Removes a tag from current Image.
     */
    @FXML
    private void handleRemoveSelectedTag() {
        ArrayList<TagInfo> assignedTagToRemove = getSelectedTag(assignedTags);

        for (TagInfo tagInfo : assignedTagToRemove) {
            Main.currentImageInfo.getImage().removeTag(tagInfo.getTag());
        }

        assignedTags.getItems().removeAll(assignedTagToRemove);
        tagLibrary.getItems().addAll(assignedTagToRemove);

        Main.currentImageInfo.updateFileInfo();
    }

    /**
     * Deletes a tag from tag library.
     */
    @FXML
    private void handleDeleteSelectedTag() {
        ArrayList<TagInfo> libraryTagToRemove = getSelectedTag(tagLibrary);

        for (TagInfo tagInfo : libraryTagToRemove) {
            TagManager.deleteTag(tagInfo.getTag());
        }

        tagLibrary.getItems().removeAll(libraryTagToRemove);
        FileInfo.updateAllInfo();
    }

    /**
     * Returns the Tag which is currently selected in the list of all Tags.
     *
     * @param tags List of all the Tags as a TagInfo
     * @return The currently selected Tag
     */
    private ArrayList<TagInfo> getSelectedTag(ListView<TagInfo> tags) {
        ArrayList<TagInfo> selectedTags = new ArrayList<>();
        for (TagInfo tagInfo : tags.getItems()) {
            if (tagInfo.isSelected()) {
                selectedTags.add(tagInfo);
            }
        }
        return selectedTags;
    }

    /**
     * Adds a tag to current Image.
     */
    @FXML
    private void handleAddSelectedTag() {
        ArrayList<TagInfo> toAdd = new ArrayList<>();
        for (TagInfo tagInfo : tagLibrary.getItems()) {
            if (tagInfo.isSelected()) {
                addTag(tagInfo);
                toAdd.add(tagInfo);
            }
        }
        tagLibrary.getItems().removeAll(toAdd);
        Main.currentImageInfo.updateFileInfo();
    }

    /**
     * Adds a new tag from its tag info.
     *
     * @param tagInfo: info of tag being added
     */
    private void addTag(TagInfo tagInfo) {
        Main.currentImageInfo.getImage().assignTag(tagInfo.getTag());
        Main.currentImageInfo.updateFileInfo();
        assignedTags.getItems().add(tagInfo);
    }

    /**
     * Updates tags displayed on screen.
     *
     * @param listView listView seen by user
     * @param tags     Tags to be added to add in listView
     */
    private void updateListView(ListView<TagInfo> listView, ArrayList<Tag> tags) {
        ArrayList<TagInfo> tagInfoList = new ArrayList<>();
        for (Tag tag : tags) {
            tagInfoList.add(new TagInfo(tag.getName(), tag));
        }
        ObservableList<TagInfo> Tags = FXCollections.observableArrayList(tagInfoList);
        listView.setItems(Tags);
        listView.setCellFactory(CheckBoxListCell.forListView(TagInfo::selectedProperty));
    }
}
