/*
package dataSource;


import util.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class DataObjectFile implements Loggable {

    protected BufferedReader br;

    protected String pathToFile;

    public DataObjectFile(String pathToFile)  {
        this.pathToFile = pathToFile;
    }

    public void openForRead() {

        Path path = null;

        try {
            logger.info("Opening the file");
            path = Paths.get(pathToFile);

            br = Files.newBufferedReader(path);

            logger.info(String.format("File %s opened for reading", path.getFileName()));
        } catch (InvalidPathException ipE) {
            logger.error(String.format("File not found in %s", pathToFile));

        } catch (IOException ioE) {
            logger.error(String.format("Cannot read file %s", path.getFileName()));
            ioE.printStackTrace();
        }
    }


    public Stream<String> getLines() {
        if (br == null) {
            logger.error("File not opened");
            return null;
        }

        return br.lines();
    }

}
*/
