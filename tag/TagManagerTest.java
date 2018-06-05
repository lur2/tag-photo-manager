package pack.tag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pack.image.Image;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for TagManager.
 */
class TagManagerTest {
    private Image testImage;
    private String properName = "TestImage.jpg";
    private File file = new File(properName).getAbsoluteFile();

    /**
     * Opens the test image before every test.
     */
    @BeforeEach
    void setUp() {
        testImage = new Image(file);
    }

    /**
     * Resets global list of Tags after every test.
     * Renames the test image to its original name at the end of every test.
     */
    @AfterEach
    void tearDown() {
        TagManager.allTags.clear();
        File fixFile = testImage.getFile();
        fixFile.renameTo(file);
    }

    /**
     * Tests to see if createTag() works.
     */
    @Test
    void createTag() {
        setUp();
        TagManager.createTag("Red");
        assertEquals(1, TagManager.allTags.size());
        assertEquals("Red", TagManager.allTags.get(0).getName());
        tearDown();
    }

    /**
     * Tests to see if getTag() creates new tag if tag doesn't already exist.
     */
    @Test
    void getTagCreate() {
        TagManager.getTag("Red");
        assertEquals(1, TagManager.allTags.size());
        assertEquals("Red", TagManager.allTags.get(0).getName());
        tearDown();
    }

    /**
     * Tests to see if getTag() works.
     */

    @Test
    void getTag() {
        setUp();
        Tag tag = new Tag("Red");
        TagManager.allTags.add(tag);
        assertEquals(tag, TagManager.getTag("Red"));
        tearDown();
    }

    /**
     * Tests to see if getAvailableTags() works.
     */
    @Test
    void getAvailableTags() {
        setUp();
        Tag tag = new Tag("Red");
        TagManager.allTags.add(tag);
        Image image = new Image(new File("TestImage.jpg"));
        ArrayList<Tag> list = new ArrayList<>();
        list.add(tag);
        assertEquals(list, TagManager.getAvailableTags(image));
        tearDown();
    }

    /**
     * Tests to see if getAllTags() works.
     */
    @Test
    void getAllTags() {
        setUp();
        Tag tag = new Tag("Red");
        TagManager.allTags.add(tag);
        ArrayList<Tag> list = new ArrayList<Tag>();
        list.add(tag);
        assertEquals(list, TagManager.getAllTags());
        assertEquals(1, TagManager.getAllTags().size());
        tearDown();
    }

    /**
     * Tests to see if getAllTags() works for multiple tags.
     */
    @Test
    void getAllTagsMultiple() {
        setUp();
        Tag tag1 = new Tag("Red");
        Tag tag2 = new Tag("White");
        TagManager.allTags.add(tag1);
        TagManager.allTags.add(tag2);
        ArrayList<Tag> list = new ArrayList<Tag>();
        list.add(tag1);
        list.add(tag2);
        assertEquals(list, TagManager.getAllTags());
        assertEquals(2, TagManager.getAllTags().size());
        tearDown();
    }

    /**
     * Tests to see if getAvailableTags() works with no tags.
     */
    @Test
    void getAllTagsEmpty() {
        setUp();
        ArrayList<Tag> list = new ArrayList<Tag>();
        assertEquals(list, TagManager.getAllTags());
        assertEquals(0, TagManager.getAllTags().size());
        tearDown();
    }

    /**
     * Tests to see if getTagsFromFileName() works.
     */
    @Test
    void getTagsFromFileName() {
        setUp();
        Tag tag = TagManager.getTag("Red");
        testImage.assignTag(tag);
        ArrayList<Tag> list = new ArrayList<>();
        list.add(tag);
        assertEquals(list, TagManager.getTagsFromFileName(testImage.getFile().getAbsolutePath()));
        tearDown();
    }

    /**
     * Tests to see if resetImagesFromTags() works.
     */
    @Test
    void resetImagesFromTags() {
        setUp();
        Tag tag = TagManager.getTag("Red");
        testImage.assignTag(tag);
        TagManager.resetImagesFromTags();
        assertEquals(new ArrayList<Image>(), tag.getImages());
        tearDown();
    }

    /**
     * Tests to see if searchTags() works.
     */
    @Test
    void searchTags() {
        setUp();
        Tag tag = TagManager.getTag("Red");
        testImage.assignTag(tag);
        ArrayList<Image> list = new ArrayList<>();
        list.add(testImage);
        assertEquals(list, tag.getImages());
        tearDown();
    }
}