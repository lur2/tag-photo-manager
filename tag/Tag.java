package pack.tag;

import pack.image.Image;

import java.util.ArrayList;

/**
 * Class keeping track of one tag's info.
 */
public class Tag implements java.io.Serializable {
    /**
     * Tag text displayed in Image names.
     */
    private String name;

    /**
     * Every Image this tag has been assigned to.
     */
    private ArrayList<Image> images = new ArrayList<>();

    /**
     * Constructs a tag with name text.
     *
     * @param text: Name of this tag
     */
    public Tag(String text) {
        this.name = text;
    }

    /**
     * Returns the name of this Tag.
     *
     * @return this Tag's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an array list of all images this tag is assigned to. This is used to search through images for tag use.
     *
     * @return ArrayList of Images with this tag assigned to it
     */
    public ArrayList<Image> getImages() {
        return images;
    }


    /**
     * Resets the image list when the directory is updated.
     */
    void resetImages() {
        images.clear();
    }


    /**
     * Remove the image from its list of images that the tag is attached to.
     *
     * @param image: the image that removed the tag
     */
    public void removeImage(Image image) {
        if (images.contains(image)) {
            images.remove(image);
        }
    }

    /**
     * Add the image to its list of images after the image has been assigned with the tag.
     *
     * @param image: the image that has been assigned with the tag
     */
    public void addImage(Image image) {
        if (!images.contains(image)) {
            images.add(image);
        }
    }
}
