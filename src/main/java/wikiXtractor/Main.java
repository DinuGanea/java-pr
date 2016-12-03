package wikiXtractor;

import wikiXtractor.factory.PageFactory;
import wikiXtractor.model.Page;
import wikiXtractor.output.PageExport;
import wikiXtractor.util.Loggable;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

/**
 * The outstanding MAIN CLAAAASSS :D
 * <p>
 * Defines the entire workflow
 * <p>
 * This will grow into a strong Monolith.
 *
 * @author Dinu Ganea
 */
public class Main implements Loggable {

    public static final String DOMAIN_NAME = "wikiXtractor";

    public static void main(String args[]) {

        // The last try-catch hope of this application. Log every exception that it's thrown and print the stack trace
        try {

            // Mark the start of a new session
            // Nice to have a delimited logging for each run
            logger.info("==========================================================");
            logger.info("====================STARTING A NEW RUN====================");
            logger.info("==========================================================");

            // input
            String input = args[0];
            // output
            String output = args[1];

            // Create the path
            Path path = Paths.get(input);

            // get the pages
            Set<Page> pages = PageFactory.build(path);

            // create an export instance
            PageExport ep = new PageExport(output);

            // export files to xml
            ep.exportToXML(pages);

        } catch (ArrayIndexOutOfBoundsException e) {
            // Catch explicitly the input parameter exception
            logger.error("Please make sure to provide the path to the input file and the name of the output file");
        } catch (Exception e) {
            logger.error("An error occurred!");
            logger.error(e.getMessage(), e);
        }

    }
}
