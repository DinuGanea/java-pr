package factory;

import extractors.LinkExtraction;
import model.Page;
import util.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class PageFactory implements Loggable {

    private static final char DELIMITER_CHAR = '¤';

    /**
     * Open a buffered stream for the filepath
     *
     * @param pathToFile - path to the file that is being imported
     * @return - buffered reader objects
     * @throws Exception
     */
    protected static BufferedReader openForRead(String pathToFile) throws Exception {

        Exception e;

        Path path = null;

        /**
         * A BufferedReader object will be used to read the file line by line. We don't want to load it completely into RAM
         */
        BufferedReader reader;

        try {
            logger.info("Try to open the file");
            path = Paths.get(pathToFile);

            reader = Files.newBufferedReader(path);

            logger.info(String.format("File %s opened for reading", path.getFileName()));

            return reader;

        } catch (InvalidPathException ipE) {
            logger.error(String.format("File not found in %s", pathToFile));
            e = ipE;

        } catch (IOException ioE) {
            logger.error(String.format("Cannot read file %s", path.getFileName()));
            e = ioE;
        }

        throw e;
    }


    /**
     * Read the file and convert each entry to a page object
     *
     * @param pathToFile - path to the source file
     * @return - set of page objects
     * @throws Exception
     */
    public static Set<Page> build(String pathToFile) throws Exception {

        // instantiate the reader object
        BufferedReader reader = openForRead(pathToFile);

        Iterator<String> iterator = reader.lines().iterator();

        Set<Page> pages = new HashSet<>();

        StringBuilder rawPageHtml = null;

        Page page = new Page();

        logger.info("Reading the source file line by line");

        // iterate through file's lines
        while (iterator.hasNext()) {
            String line = iterator.next();

            // If line string contains the delimiter char, should be checked if it's the begging or end of the html page
            if (line.indexOf(DELIMITER_CHAR) >= 0) {
                // the line string contains only one char (the delimiter char)
                // this means that we should empty the content holder and add the page (if not null) to the return set
                if (line.length() == 1) {

                    if (rawPageHtml != null) {
                        Set<String> categories = LinkExtraction.getCategories(rawPageHtml.toString());
                        page.setCategories(categories);
                    }

                    pages.add(page);

                    page = null;
                    rawPageHtml = null;
                    // we're done here
                    continue;
                } else {
                    // instantiate a new string builder object (works faster then a simple String object), and spline the header line
                    rawPageHtml = new StringBuilder();
                    String[] info = line.split("\t");

                    // the position of the following elements are by default defined. if they are changed in the source file
                    // these indexes should be modified
                    String articleID = info[1];
                    String namespaceID = info[2];
                    String articleName = info[3];

                    // create a new page object
                    page = new Page(namespaceID, articleID, articleName);

                    // get categories
                    // Todo: use LinkExtraction class (waiting for the commit)
                }
            }

            // append line to the string builder (the actual html page)
            rawPageHtml.append(line);
        }


        logger.info(String.format("%d page objects successfully created.", pages.size()));

        return pages;
    }

}
