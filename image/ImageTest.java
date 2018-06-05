package pack.image;

import pack.tag.Tag;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the Image class.
 */
class ImageTest {
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
     * Renames the test image to its original name at the end of every test.
     */
    @AfterEach
    void tearDown() {
        File fixFile = testImage.getFile();
        fixFile.renameTo(file);
    }

    /**
     * Tests to see if getName() works.
     */
    @Test
    void getName() {
        setUp();
        assertEquals("TestImage", testImage.getName());
        tearDown();
    }

    /**
     * Tests to see if getFile() works.
     */
    @Test
    void getFile() {
        setUp();
        assertEquals(file, testImage.getFile());
    }

    /**
     * Tests to see if assignTag() works by trying to assign tag and checking that the name of the file now includes
     * that tag.
     */
    @Test
    void assignTag() {
        setUp();
        Tag tag = new Tag("Keyboard");
        testImage.assignTag(tag);
        assertEquals("TestImage @Keyboard", testImage.getName());
        tearDown();
    }

    /**
     * Test to see if removeTag() is removing the tag provided.
     */
    @Test
    void removeTag() {
        setUp();
        Tag tag = new Tag("Keyboard");
        testImage.assignTag(tag);
        testImage.removeTag(tag);
        assertEquals("TestImage", testImage.getName());
        tearDown();
    }

    /**
     * Test to see if removeTag() does nothing if the wrong tag is entered.
     */
    @Test
    void removeTagWrongName() {
        setUp();
        Tag tag = new Tag("Keyboard");
        testImage.assignTag(tag);
        testImage.removeTag(new Tag("Not Keyboard"));
        assertEquals("TestImage @Keyboard", testImage.getName());
        tearDown();
    }

    /**
     * Test to see if removeTag() does nothing if a different Tag with the same name is entered.
     */
    @Test
    void removeTagSameName() {
        setUp();
        Tag tag = new Tag("Keyboard");
        testImage.assignTag(tag);
        testImage.removeTag(new Tag("Keyboard"));
        assertEquals("TestImage @Keyboard", testImage.getName());
        tearDown();
    }

    /**
     * Test to see if getAssignedTags() returns all the tags assigned to test image.
     */
    @Test
    void getAssignedTags() {
        setUp();
        ArrayList<Tag> tags = new ArrayList<>();
        Tag tag1 = new Tag("Keyboard");
        Tag tag2 = new Tag("Red");
        Tag tag3 = new Tag("White");
        tags.add(tag1);
        tags.add(tag2);
        tags.add(tag3);
        testImage.assignTag(tag1);
        testImage.assignTag(tag2);
        testImage.assignTag(tag3);
        assertEquals(tags, testImage.getAssignedTags());
        tearDown();
    }

    /**
     * Test to see if getRenamingList() includes all previously used names.
     */
    @Test
    void getRenamingList() {
        Tag tag1 = new Tag("Keyboard");
        Tag tag2 = new Tag("Red");
        Tag tag3 = new Tag("White");
        testImage.assignTag(tag1);
        testImage.assignTag(tag2);
        testImage.assignTag(tag3);
        ArrayList<String> names = new ArrayList<>();
        names.add("TestImage");
        names.add("TestImage @Keyboard");
        names.add("TestImage @Keyboard @Red");
        names.add("TestImage @Keyboard @Red @White");
        assertEquals(names, testImage.getRenamingList());
        tearDown();
    }

    /**
     * Test to see that revert() correctly changes the name of the test image and doesn't add itself to the
     * renamingList.
     */
    @Test
    void revert() {
        Tag tag1 = new Tag("Keyboard");
        Tag tag2 = new Tag("Red");
        testImage.assignTag(tag1);
        testImage.assignTag(tag2);
        testImage.revert("TestImage @Keyboard");
        assertEquals("TestImage @Keyboard", testImage.getName());
        assertEquals(3, testImage.getRenamingList().size());
        tearDown();
    }

    /**
     * Test to see that revert() does nothing when a previous name isn't entered.
     */
    @Test
    void revertWrong() {
        Tag tag1 = new Tag("Keyboard");
        Tag tag2 = new Tag("Red");
        testImage.assignTag(tag1);
        testImage.assignTag(tag2);
        testImage.revert("TestImage @Red");
        assertEquals("TestImage @Keyboard @Red", testImage.getName());
        tearDown();
    }
}