package pack.image;

import pack.ImageLogger;
import pack.tag.Tag;
import pack.tag.TagManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for managing a single Image and its properties, also Serializable.
 */
public class Image implements java.io.Serializable {

    /**
     * List of all names this Image has had.
     */
    private ArrayList<String> renamingList = new ArrayList<>();

    /**
     * Original Filename for this Image.
     */
    private String originalFilename;

    /**
     * Current Filename for this Image.
     */
    private String name;

    /**
     * The file represented by this Image.
     */
    private File file;

    /**
     * The file extension of this Image.
     */
    private String fileExtension;

    /**
     * All the tags assigned to this Image.
     */
    private ArrayList<Tag> assignedTags = new ArrayList<>();

    /**
     * Construct Image from a preexisting File.
     *
     * @param imageFile File this Image is constructed from
     */
    public Image(File imageFile) {
        String filename = imageFile.getName();
        int indexOfFileExtension = filename.lastIndexOf(".");
        int indexOfTags = filename.indexOf(" @");

        this.file = imageFile.getAbsoluteFile();
        this.fileExtension = filename.substring(indexOfFileExtension);
        this.name = filename.substring(0, indexOfFileExtension);    // Get its file name excluding the file extension

        // If the image file already has tags, save its original file name without the tags and assign the tags.
        if (indexOfTags > -1) {
            this.originalFilename = filename.substring(0, indexOfTags);
            this.assignedTags = TagManager.getTagsFromFileName(filename);
            for (Tag tag : assignedTags) {
                tag.addImage(this);
            }
        } else { // Otherwise, its original file name is same as its current name
            this.originalFilename = this.name;
        }

        this.renamingList.add(this.name);
    }

    /**
     * Returns the original file name without the tags.
     *
     * @return The original file name without the tags.
     */
    String getNameWithoutTags() {
        return this.originalFilename;
    }

    /**
     * Returns the current file name of this Image.
     *
     * @return Name of this Image
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the file extension of this image.
     *
     * @return File extension of this image
     */
    String getFileExtension() {
        return this.fileExtension;
    }

    /**
     * Returns the File this Image was constructed from.
     *
     * @return File of this Image
     */
    public File getFile() {
        return file;
    }

    /**
     * Updates the file after the image is moved.
     *
     * @param newFile the image file that has been moved
     */
    void setFile(File newFile) {
        file = newFile;
    }

    /**
     * Adds a Tag to this Image.
     *
     * @param tag Tag to be added
     */
    public void assignTag(Tag tag) {
        //If the tag already exists, do not add duplicates
        if (assignedTags.contains(tag)) {
            return;
        }
        assignedTags.add(tag);
        tag.addImage(this);
        this.rename();
    }

    /**
     * Removes a Tag from this Image.
     *
     * @param tag Tag to be removed
     */
    public void removeTag(Tag tag) {
        if (this.assignedTags.contains(tag)) {
            this.assignedTags.remove(tag);
            tag.removeImage(this);
            this.rename();
        }
    }

    /**
     * Returns an ArrayList of all tags currently assigned to this Image.
     *
     * @return Tags assigned to this Image
     */
    public ArrayList<Tag> getAssignedTags() {
        return assignedTags;
    }

    /**
     * Renames Image to take into account the new tags. Doesn't have any parameters because it builds the name based off
     * the original filename and all the tags currently associated to this Image.
     */
    private void rename() {
        // Make name this file will have
        StringBuilder generatedName = new StringBuilder(originalFilename);
        for (Tag tag : assignedTags) {
            generatedName.append(" @").append(tag.getName());
        }

        // Rename the file
        String file_dir_pathname = file.getParentFile().getPath() + File.separator;
        String newName = "";
        // if the renaming isn't successful, add a number and try again
        int tries = 0;
        boolean renaming_is_successful = false;
        while (!renaming_is_successful) {
            String suffix = "";
            if (tries > 0) {
                suffix = "(" + Integer.toString(tries) + ")";
            }
            newName = generatedName.toString() + suffix;
            File newly_named_File = new File(file_dir_pathname + newName + this.fileExtension);
            if (!newly_named_File.exists()) {
                renaming_is_successful = file.renameTo(newly_named_File);
                file = newly_named_File;
            }
            tries++;
        }

        // Keep log of change
        try {
            ImageLogger.log(name, newName, fileExtension);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // if name previously existed, don't add to renamingList
        name = newName;
        for (String pastName : renamingList) {
            if (pastName.equals(name)) {
                return;
            }
        }
        this.renamingList.add(name);
    }

    /**
     * Returns all the names this Image has ever had, including its original name, but excluding multiple uses of the
     * same name.
     *
     * @return This Image's list of all name changes
     */
    public ArrayList<String> getRenamingList() {
        return renamingList;
    }

    /**
     * Reverts the name of this Image to a previous name from the renaming list.
     *
     * @param oldName Previous name from the renaming list that is being changed back to
     */
    public void revert(String oldName) {
        // If the name has never been previously used do nothing
        if (!this.renamingList.contains(oldName)) {
            return;
        }

        String[] oldTags = oldName.substring(this.originalFilename.length()).split(" @");
        ArrayList<Tag> newTags = new ArrayList<>();

        // Ensures tags get updated without making new ones
        for (int i = 1; i < oldTags.length; i++) {
            Tag tag = TagManager.getTag(oldTags[i]);
            newTags.add(tag);
            if (!assignedTags.contains(tag)) {
                tag.getImages().add(this);
            }
        }
        for (Tag tag : assignedTags) {
            if (!newTags.contains(tag)) {
                tag.getImages().remove(this);
            }
        }
        assignedTags = newTags;
        this.rename();
    }
}
