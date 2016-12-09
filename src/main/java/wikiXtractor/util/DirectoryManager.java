package wikiXtractor.util;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryManager {

    public static String getFullPath(String uri) {
        Path absolutePath = Paths.get(uri).toAbsolutePath();
        return absolutePath.toString();
    }


    public static String fullPathToURL(String fullPath) {
        return String.format("file:%s", fullPath);
    }


    public static void cleanDir(String uri) {
        try {
            FileUtils.cleanDirectory(new File(getFullPath(uri)));
        } catch (IOException e) {
            e.printStackTrace();
            // Nothing to be done. if the inventory doesn't exists, it's automatically clean :)
        }
    }

}
