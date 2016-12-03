package wikiXtractor.factory;

import wikiXtractor.extractors.LinkExtraction;
import wikiXtractor.model.Page;
import wikiXtractor.extractors.LinkExtraction;
import wikiXtractor.model.Page;
import wikiXtractor.util.Loggable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Open the input file for reading and create page objects out of it.
 *
 * @author Dinu Ganea
 */
public class PageFactory implements Loggable {

    // Used to specify the max amount of read pages (used currently only to log the progress)
    private static final int PAGE_BLOCK_SIZE = 10000;

    // Path to the categories in html article (css selector)
    // Could be declared in LinkExtraction, but we try to keep that class as context-independent as possible
    private static final String DEFAULT_CATS_SELECTOR = "div#catlinks ul li a";

    // Marks the begin/end of an article
    private static final char LINE_DELIMITER_CHAR = '¤';

    // Separates head line values of an article (page)
    private static final String VALUES_DELIMITER = "\t";

    /**
     * Open a buffered stream for the filepath
     *
     * @param path path object to the file that is being imported
     * @return buffered reader objects
     * @throws Exception
     */
    protected static BufferedReader openForRead(Path path) throws Exception {

        Exception e;

        /**
         * A BufferedReader object will be used to read the file line by line. We don't want to load it completely into RAM
         */
        BufferedReader reader;

        try {
            logger.info("Try to open the file");

            // Open a stream to the input file
            reader = Files.newBufferedReader(path);

            logger.info(String.format("File \"%s\" opened for reading", path.getFileName()));

            return reader;

        } catch (InvalidPathException ipE) {
            logger.error(String.format("File not found in \"%s\"", path.normalize().toAbsolutePath()));
            e = ipE;

        } catch (IOException ioE) {
            logger.error(String.format("Cannot read file \"%s\"", path.getFileName()));
            e = ioE;
        }

        // throw the exception further. should be caught outside this method for more information
        throw e;
    }


    /**
     * Read the file and convert each entry to a page object
     *
     * @param path path object to the source file
     * @return set of page objects
     * @throws Exception
     */
    public static Set<Page> build(Path path) throws Exception {

        // instantiate the reader object
        BufferedReader reader = openForRead(path);

        Iterator<String> iterator = reader.lines().iterator();

        logger.info("Reading the source file line by line and creating page objects");

        // set of pages to be returned
        Set<Page> pages = new HashSet<>();

        // will contain a single html article
        StringBuilder rawPageHtml = null;

        // declaration of the page object
        Page page = null;

        // Counter used for logging
        int pageCounter = 0;

        // iterate through file's lines
        while (iterator.hasNext()) {
            // read the line
            String line = iterator.next();

            // If line string contains the delimiter char, should be checked if it's the begging or end of the html page
            if (line.indexOf(LINE_DELIMITER_CHAR) >= 0) {
                // the line string contains only one char (the delimiter char)
                // this means that we should empty the content holder and add the page (if not null) to the return set
                if (line.length() == 1) {
                    // by input definition, the page object shouldn't be null here
                    if (page == null) {
                        // even if it happens for the page to be null, skip this routine and show a warning message
                        logger.warn("Null page object found. Please check the input file");
                        continue;
                    }

                    // time to get extract the categories
                    Set<String> categories = LinkExtraction.extractCategories(rawPageHtml.toString(), DEFAULT_CATS_SELECTOR, page.getPageID());

                    // Todo: don't forget to add category objects
                    //page.setCategories(categories);

                    page.setHtmlContent(rawPageHtml.toString());

                    // Log block-wise number of pages that were till now converted
                    if(++pageCounter % PAGE_BLOCK_SIZE == 0) {
                        logger.info(String.format("%d page objects created", pageCounter));
                    }

                    pages.add(page);

                    // release these objects. it's not guaranteed that the GC will do it's job right away, but
                    // try a sweet little RAM optimisation. (WE LOVE YOU RAM)
                    page = null;
                    rawPageHtml = null;

                    // we're done here
                    continue;
                } else {
                    // instantiate a new string builder object (works faster then a simple String object), and spline the header line
                    rawPageHtml = new StringBuilder();

                    // split the string line into one array. the delimiter should represent the one from the input file
                    String[] info = line.split(VALUES_DELIMITER);

                    // the position of the following elements are by default defined. if they are changed in the source file
                    // these indexes should be modified too
                    String articleID = info[1];
                    int namespaceID = Integer.parseInt(info[2]);
                    String articleName = info[3];

                    // create a new page object
                    page = new Page(namespaceID, articleID, articleName);
                }
            }

            // should actually never occur. A check is never bad though.
            if (rawPageHtml == null) {
                logger.warn("Oooops. HTML container is somehow empty. Please check the input file");
                continue;
            }

            // append line to the string builder (the actual html page)
            rawPageHtml.append(line);
        }


        logger.info(String.format("A total of %d page objects successfully created.", pages.size()));

        return pages;
    }

}
