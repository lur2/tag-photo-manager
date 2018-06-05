package pack.image;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import pack.tag.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Medium class to wrap information of a file into a TreeTableView and also modify Image wrapped dynamically.
 */
public class FileInfo {

    /**
     * File name.
     */
    private StringProperty name;

    /**
     * Tags on file as string.
     */
    private StringProperty tags = new SimpleStringProperty("");

    /**
     * Whether or not this file is an Image.
     */
    private boolean isImage;

    /**
     * File associated to this info.
     */
    private File file;

    /**
     * If this is file is an image, then this is the Image associated to the file.
     */
    private Image image;

    /**
     * The ArrayList of all Fileinfo.
     */
    private static ArrayList<FileInfo> allImageInfo = new ArrayList<>();

    /**
     * Constructs FileInfo from File.
     *
     * @param file File source for this FileInfo
     */
    FileInfo(File file) {
        this.name = new SimpleStringProperty(file.getName());
        this.isImage = false;
        this.file = file;
    }

    /**
     * Constructs FileInfo from Image.
     *
     * @param image Image source for this FileInfo
     */
    FileInfo(Image image) {
        this.name = new SimpleStringProperty(image.getName());
        this.isImage = true;
        this.file = image.getFile();
        this.image = image;
        this.tags.setValue(tagsToString(image.getAssignedTags()));
        allImageInfo.add(this);
    }

    /**
     * Updates the name and tags of this FileInfo.
     */
    public void updateFileInfo() {
        if (this.isImage()) {
            this.setTags(tagsToString(this.image.getAssignedTags()));
            this.setName(this.image.getName());
        }
    }

    public static void updateAllInfo() {
        for (FileInfo fileInfo : allImageInfo) {
            fileInfo.updateFileInfo();
        }
    }

    /**
     * Turns tags into a string.
     *
     * @param tagList List of tags being turned into a string
     * @return Tags as string
     */
    private String tagsToString(ArrayList<Tag> tagList) {
        ArrayList<String> list = new ArrayList<>();
        for (Tag tag : tagList) {
            list.add(tag.getName());
        }
        Collections.sort(list);
        StringBuilder tags = new StringBuilder();
        for (String tagName : list) {
            tags.append(tagName).append(" ");
        }
        return tags.toString();
    }

    /**
     * Returns whether or not this file is an Image.
     *
     * @return True if Image, False if not Image
     */
    public boolean isImage() {
        return isImage;
    }

    /**
     * Returns name of this file.
     *
     * @return Name of this FileInfo as StringProperty
     */
    StringProperty nameProperty() {
        return name;
    }

    /**
     * Sets Name of this File.
     *
     * @param name New name wanted for this FileInfo
     */
    private void setName(String name) {
        this.name.set(name);
    }

    /**
     * Returns all the tags of this FileInfo.
     *
     * @return tags of this FileInfo as StringProperty
     */
    StringProperty tagsProperty() {
        return tags;
    }

    /**
     * Sets tags of this File Info.
     *
     * @param tags String of tags to be assigned to this FileInfo
     */
    private void setTags(String tags) {
        this.tags.set(tags);
    }

    /**
     * If applicable, returns the Image of ths FileInfo.
     *
     * @return Image of this FileInfo or null if no Image is associated to this FileInfo
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the File of this FileInfo.
     *
     * @return File from which this FileInfo is derived
     */
    File getFile() {
        return file;
    }

    @Override
    public String toString() {
        if (isImage()) {
            return "FileInfo{" + "name=" + name + ", tags=" + tags + '}';
        } else {
            return "FileInfo{" + "name=" + name + ", path=" + file.getPath() + '}';
        }
    }
}
