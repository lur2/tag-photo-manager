package pack;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pack.image.FileInfo;
import pack.image.ImageManager;
import pack.tag.TagManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Main class initializing the application.
 */
public class Main extends Application {

    /**
     * FileInfo for current Image selected by application.
     */
    public static FileInfo currentImageInfo;

    /**
     * Starts application.
     *
     * @param primaryStage stage being used by application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root =
                FXMLLoader.load(
                        getClass()
                                .getResource("image"
                                        + File.separator
                                        + "ImageManagerUI.fxml"));
        primaryStage.setOnCloseRequest(event -> {
            // store what happened in application
            storeMemory();
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("Image Manager");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        // sets-up application for use
        initialize();
        // launch application
        launch(args);
        // store what happened in application
        storeMemory();
    }

    /**
     * Initializes the settings.
     */
    private static void initialize() {
        // Read the configuration file
        String[] configParameters = {"directory", "viewMode", "tagList"};
        Map<String, String> settings = readConfigFile(configParameters); // Configuration settings

        // Configure the environment.
        TagManager.configureTagList(settings.getOrDefault("tagList", ""));
        ImageManager.configureDirectory(settings.getOrDefault("viewMode", "viewTree"));
        ImageManager.configureDirectory(settings.getOrDefault("directory", System.getProperty("user.home")));
    }

    /**
     * Stores the program memory.
     */
    private static void storeMemory() {
        // set the setting values
        Map<String, String> settings = new HashMap<>();
        settings.put("directory", ImageManager.getConfigDirectory());
        settings.put("viewMode", ImageManager.getConfigViewMode());
        settings.put("tagList", TagManager.getConfigTagList());

        writeConfigFile(settings);
    }

    /**
     * Reads the configuration file if it exists and set the program.
     *
     * @param parameters parameters of the configuration settings
     * @return map of configuration settings
     */
    private static Map<String, String> readConfigFile(String[] parameters) {
        File configFile = new File("config.properties");

        // If the config file does not exists, do not try loading the file
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new HashMap();
        }

        // Otherwise, load the file and read the settings from the file
        Map<String, String> settings = new HashMap<>(); // Configuration settings

        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream("config.properties"));

            // Read the configuration file and store the settings
            for (int i = 0; i < parameters.length; i++) {
                String parameter = parameters[i];
                settings.put(parameter, prop.getProperty(parameter, null));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return settings;
    }

    /**
     * Writes a configuration file with current setting.
     *
     * @param settings The map of current settings
     */
    private static void writeConfigFile(Map<String, String> settings) {
        try {
            Properties prop = new Properties();

            // set the properties value
            for (String parameter : settings.keySet()) {
                prop.setProperty(parameter, settings.get(parameter));
            }

            // save properties to project root folder
            prop.store(new FileOutputStream("config.properties"), null);

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

}
