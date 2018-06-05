package pack.tag;

import pack.image.Image;
import pack.image.ImageManager;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Manages the Tag Class.
 */
public class TagManager {
    /**
     * Keeps track of all tags in list from user.
     */
    static ArrayList<Tag> allTags = new ArrayList<>();

    /**
     * Gets configuration data of TagManagers.
     *
     * @return A String format of tag names
     */
    public static String getConfigTagList() {
        String tagListData = "";
        for (Tag tag : allTags) {
            tagListData = tagListData + "@" + tag.getName();
        }
        return tagListData;
    }

    /**
     * Configures TagManager using the tag list data from a configuration file.
     *
     * @param tagListData a string format of tag list data
     */
    public static void configureTagList(String tagListData) {
        if (tagListData.indexOf("@") < 0) {
            return;
        }

        //Get unique values and make tags
        String[] tagNames = Arrays.stream(tagListData.split("@")).distinct().toArray(String[]::new);
        for (int i = 1; i < tagNames.length; i++) {
            createTag(tagNames[i]);
        }
    }


    /**
     * Creates a tag with name tagName.
     *
     * @param tagName Name of wanted tag
     * @return new Tag with name tagName
     */
    static Tag createTag(String tagName) {
        Tag newTag = new Tag(tagName);
        allTags.add(newTag);
        return newTag;
    }

    /**
     * Returns tag with name tagName, if it doesn't exist it first creates the tag.
     *
     * @param tagName Name of wanted tag
     * @return Tag being searched for
     */
    public static Tag getTag(String tagName) {
        // If the tag exists, return the existing tag
        for (Tag tag : allTags) {
            if (tag.getName().equals(tagName)) {
                return tag;
            }
        }
        // Otherwise, make a new Tag and return the newly made tag
        return createTag(tagName);
    }


    /**
     * Removes this tag from list of known tags.
     *
     * @param tag tag to be deleted from the pool of tags
     */
    static void deleteTag(Tag tag) {
        allTags.remove(tag);
        ImageManager.removeTagFromAllImages(tag);
    }

    /**
     * Returns list of all available tags to add to an image.
     *
     * @param image Image where we want to find tags
     * @return List of all available tags
     */
    static ArrayList<Tag> getAvailableTags(Image image) {
        ArrayList<Tag> availableTags = new ArrayList<>();
        ArrayList<Tag> assignedTags = image.getAssignedTags();
        for (Tag tag : allTags) {
            if (!assignedTags.contains(tag)) {
                availableTags.add(tag);
            }
        }
        return availableTags;
    }

    /**
     * Returns a list of all Images that a Tag has been associated to.
     *
     * @param searchingTag Tag being searched for
     * @return Images with this Tag
     */
    public static ArrayList<Image> searchTags(String searchingTag) {
        for (Tag tag : allTags) {
            if (tag.getName().equals(searchingTag)) {
                return tag.getImages();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Returns the list of tags that are already attached to the image based on the image's file name.
     *
     * @param filename The file name of the image
     * @return List of tags that are attached to the image file
     */
    public static ArrayList<Tag> getTagsFromFileName(String filename) {
        ArrayList<Tag> tagsFromName = new ArrayList<>();
        String[] tagNames = filename.substring(filename.indexOf("@") + 1,
                filename.lastIndexOf(".")).split(" @");

        for (String tagName : tagNames) {
            tagsFromName.add(getTag(tagName));
        }

        return tagsFromName;
    }

    /**
     * Returns an ArrayList of all the Tags that are in the program.
     *
     * @return All Tags in the program
     */
    static ArrayList<Tag> getAllTags() {
        return allTags;
    }

    /**
     * Removes all Images from this Tag's list of Images.
     */
    public static void resetImagesFromTags() {
        for (Tag tag : allTags) {
            tag.resetImages();
        }
    }
}
