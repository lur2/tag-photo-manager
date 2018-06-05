package pack.image;

import pack.tag.Tag;
import pack.tag.TagManager;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.util.ArrayList;

/**
 * Class for managing Images.
 */
public class ImageManager {

    /**
     * Current directory, defaulted by OS.
     */
    private static File directory = new File(System.getProperty("user.home"));

    /**
     * ArrayList of subdirectories in this directory.
     */
    private static ArrayList<File> subDirectories = new ArrayList<>();

    /**
     * ArrayList of images in this directory.
     */
    private static ArrayList<Image> images = new ArrayList<>();

    /**
     * View mode:
     * True: show all images
     * False: show images in the current directory only and show directories
     */
    private static boolean viewAllImages = false;


    /**
     * Sets ImageManager with configuration file.
     *
     * @param directoryPathname Path currently being used by this ImageManager
     */
    public static void configureDirectory(String directoryPathname) {
        if (directoryPathname != null) {
            File newDir = new File(directoryPathname);
            if (newDir.isDirectory()) {
                directory = newDir;
            }
        }
        setDirectory(directory);
    }

    /**
     * Gets the directory configuration data.
     *
     * @return the path of the directory that is currently in use
     */
    public static String getConfigDirectory() {
        return getDirectory().getPath();
    }

    /**
     * Get the view mode.
     *
     * @return the path of the directory that is currently in use
     */
    public static String getConfigViewMode() {
        if (viewAllImages) {
            return "viewAll";
        }
        return "viewTree";
    }


    /**
     * Sets the view mode depending on the user input.
     *
     * @param showAllImages the user input
     *                      True - show all images
     *                      False - show images in tree structure with sub directories.
     */
    static void setViewAllImages(boolean showAllImages) {
        viewAllImages = showAllImages;
        setDirectory(directory);
    }

    /**
     * Sets the directory location of this ImageManager and updates this ImageManager.
     *
     * @param file New directory to be set to
     */
    static void setDirectory(File file) {
        if (file != null && file.isDirectory()) {
            // reset this ImageManager
            resetContent();
            TagManager.resetImagesFromTags();
            // update the images
            directory = file;
            updateImageManager(directory);
        } else {
            directory = null;
        }
    }

    /**
     * Resets the list of images and directories.
     */
    private static void resetContent() {
        subDirectories.clear();
        images.clear();
    }

    /**
     * Returns the directory that is currently in use by this ImageManager.
     *
     * @return directory currently in use as a file
     */
    static File getDirectory() {
        return directory;
    }

    /**
     * Returns all sub directories below the current directory.
     *
     * @return ArrayList of the subdirectories below the current root directory
     */
    static ArrayList<File> getSubDirectories() {
        return subDirectories;
    }

    /**
     * Updates this ImageManager by the given directory.
     * If the view mode is set to show all images in and under the directory, it runs recursively to add all images
     * in an under the directory (including the subdirectories).
     *
     * @param directory Directory that this ImageManager is being updating to
     */
    private static void updateImageManager(File directory) {
        // check directory is not null
        if (directory == null || !directory.isDirectory()) {
            return;
        }
        if (isParentOfRoot()) {
            ImageManager.viewAllImages = false;
        }

        for (File file : directory.listFiles()) {
            if (file.isHidden()) {
                continue;
            }
            if (file.isDirectory()) {
                if (viewAllImages) {
                    updateImageManager(file);
                } else {
                    subDirectories.add(file);
                }
            } else if (isImage(file)) {
                Image image = new Image(file);
                images.add(image);
            }
        }
    }

    /**
     * Check if current directory is a parent of root
     *
     * @return boolean whether current directory is a parent of root
     */
    private static boolean isParentOfRoot() {
        File file = new File(System.getProperty("user.home"));
        while(file != null) {
            if (!file.getName().isEmpty()) {
                if (file.equals(ImageManager.getDirectory())) {
                    return true;
                }
            }
            file = file.getParentFile();
        }
        return false;
    }

    /**
     * Helper in updateImageManager. Checks if this file is an Image. Note that it use a predefined class
     * in javax because the method in Files has some problem.
     *
     * @param file File being checked
     */
    private static boolean isImage(File file) {
        MimetypesFileTypeMap map = new MimetypesFileTypeMap();
        map.addMimeTypes("image png tif jpg jpeg bmp");
        String type = map.getContentType(file);
        return type.split("/")[0].equals("image");
    }


    /**
     * Moves an Image to target place.
     *
     * @param image        Image being moved
     * @param newDirectory Directory Image is being moved to
     */
    static void moveImage(Image image, File newDirectory) {
        String newDirectoryPath = newDirectory.getPath() + File.separator;

        // If the image is moved to the same directory, do not move the image
        if (newDirectoryPath.equals(directory.getPath())) {
            return;
        }

        //Otherwise, move the image
        String tagsAndExtensions = image.getFileExtension();
        int indexOfTag = image.getName().indexOf("@");
        if (indexOfTag > 0) {
            tagsAndExtensions = image.getName().substring(indexOfTag) + tagsAndExtensions;
        }
        File newFile = new File(newDirectoryPath + image.getName() + tagsAndExtensions);
        //Check if the new location already has a file with the same name
        //  If the new directory has a file with the same name, modify the moved file name by adding numbers
        int tries = 0;
        while (newFile.exists()) {
            tries += 1;
            String newFileNameWithoutTags = image.getNameWithoutTags() + " (" + tries + ") " + tagsAndExtensions;
            newFile = new File(newDirectoryPath + newFileNameWithoutTags);
        }

        //Move the file
        if (image.getFile().renameTo(newFile)) {
            boolean isStillInDirectory = false;
            // If the view mode is to show all images in the directory and the subdirectories, check if the image should
            // still be shown because it moved to its descendant directory.
            if (viewAllImages) {
                isStillInDirectory = newDirectoryPath.startsWith(directory.getPath());
            }
            // If it is still in the same directory, update the file to the moved file.
            if (isStillInDirectory) {
                image.setFile(newFile);
            } else {
                // Otherwise, remove the image from the program system.
                images.remove(image);
                for (Tag tag : image.getAssignedTags()) {
                    tag.removeImage(image);
                }
            }
        }

    }

    /**
     * Returns all images currently in this ImageManager.
     *
     * @return ArrayList of Images currently in this ImageManager
     */
    static ArrayList<Image> getImages() {
        return images;
    }

    @Override
    public String toString() {
        return "ImageManager{" +
                "directory=" + directory +
                ", sub_directories=" + subDirectories +
                ", images=" + images +
                '}';
    }

    /**
     * Removes a deleted tag from all images.
     *
     * @param tag the deleted tag to be removed
     */
    public static void removeTagFromAllImages(Tag tag) {
        // A shallow copy of the image ArrayList is prepared since this list is modified during the loop.
        ArrayList<Image> assignedImages = (ArrayList<Image>) tag.getImages().clone();
        for (Image image : assignedImages) {
            image.removeTag(tag);
        }
    }
}
