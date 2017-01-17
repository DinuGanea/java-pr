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
     * Convert given URI to full directory path (creating directories if necessary)
     *
     * @param uri Relative path
     * @return Full path
     */
    public static String createFullPathTo(String uri) throws Exception {

        File output = new File(uri);
        // Make sure to construct the path to the file
        if (output.getParentFile() != null && !output.getParentFile().exists() && !output.getParentFile().mkdirs()) {
            throw new Exception("Counldn't create the full path for the given URI:  " + uri);
        }

        return getFullPath(uri);
    }


    /**
     * Get full path out of given URI
     *
     * @param uri - Relative path
     * @return - Absolute path
     */
    public static String getFullPath(String uri) throws Exception {
        try {
            Path absolutePath = Paths.get(uri).toAbsolutePath();
            return absolutePath.toString();
        } catch (Exception e) {
            throw new Exception("Counldn't get the full path for the given URI:  " + uri);
        }

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
        if (File.separatorChar != '/') {
            fullPath = fullPath.replace(File.separatorChar, '/');
        }
        if (!fullPath.startsWith("/")) {
            fullPath = "/" + fullPath;
        }

        return "file:" + fullPath;
    }


    /**
     * Remove all files and folders from given directory
     *
     * @param uri Relative path
     */
    public static void cleanDir(String uri) throws Exception {
        try {
            FileUtils.cleanDirectory(new File(createFullPathTo(uri)));
        } catch (IOException e) {
            // We're not actually writng a file
        } catch (IllegalArgumentException iae) {
            // Nothing to be done. if the inventory/file doesn 't exists, it's automatically clean :)
        }
    }

}
