package pack;

import java.io.IOException;
import java.util.logging.*;

/**
 * Class to keep a log of all renaming ever done.
 */
public class ImageLogger {

    /**
     * Keeps log of one renaming.
     *
     * @param name    Original Name of file
     * @param newName New name file is being changed to
     * @param ext     File extension
     * @throws IOException If it can't access ImageLog.log
     */
    public static void log(String name, String newName, String ext) throws IOException {
        LogManager.getLogManager().reset();
        Logger logger = Logger.getLogger("Image Name Changes");
        logger.setLevel(Level.INFO);
        FileHandler fileTxt = new FileHandler("ImageLog.log", true);
        logger.addHandler(fileTxt);
        SimpleFormatter formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.info("From: " + name + ext + " -> To: " + newName + ext);
        fileTxt.close();
    }
}
