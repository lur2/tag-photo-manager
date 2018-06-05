package pack.tag;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class to keep track of info for a tag.
 */
public class TagInfo {
    /**
     * Name of tag.
     */
    private StringProperty name;

    /**
     * Whether or not the tag is selected.
     */
    private BooleanProperty selected = new SimpleBooleanProperty(false);

    /**
     * Tag associated to info.
     */
    private Tag tag;

    /**
     * Construct this TagInfo.
     *
     * @param name Name of tag
     * @param tag  Tag for this TagInfo
     */
    TagInfo(String name, Tag tag) {
        this.name = new SimpleStringProperty(name);
        this.tag = tag;
    }

    /**
     * Returns this TagInfo's Tag.
     *
     * @return Tag associated to this TagInfo
     */
    public Tag getTag() {
        return tag;
    }

    /**
     * Returns this TagInfo's name.
     *
     * @return Tag as a String
     */
    String getName() {
        return name.get();
    }

    /**
     * Returns whether or not this TagInfo is currently selected.
     *
     * @return Boolean value of whether or not this TagInfo is currently selected
     */
    boolean isSelected() {
        return selected.get();
    }

    /**
     * Returns whether or not this TagInfo is currently selected as a BooleanProperty.
     *
     * @return BooleanProperty of whether or not this TagInfo is currently selected
     */
    BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public String toString() {
        return getName();
    }
}