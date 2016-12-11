package wikiXtractor.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Helper class
 * Handles directory operations.
 *
 * @author Ionut Urs
 */
public class DirectoryManager {

    /**
     * Convert given URI to full directory path
     *
     * @param uri Relative path
     * @return Full path
     */
    public static String getFullPath(String uri) {
        Path absolutePath = Paths.get(uri).toAbsolutePath();
        return absolutePath.toString();
    }


    /**
     * Create URL like path from given full path
     *
     * URL like paths contain file:/ prefix
     *
     * @param fullPath Full path to the source
     * @return URL like path
     */
    public static String fullPathToURL(String fullPath) {
        return String.format("file:%s", fullPath);
    }


    /**
     * Remove all files and folders from given directory
     *
     * @param uri Relative path
     */
    public static void cleanDir(String uri) {
        try {
            FileUtils.cleanDirectory(new File(getFullPath(uri)));
        } catch (IOException e) {
            // Nothing to be done. if the inventory/file doesn't exists, it's automatically clean :)
        }
    }

}
