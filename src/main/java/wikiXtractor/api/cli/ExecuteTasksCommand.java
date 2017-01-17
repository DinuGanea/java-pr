package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;
import scala.reflect.internal.Trees;
import wikiXtractor.api.Command;
import wikiXtractor.scheduler.TaskScheduler;
import wikiXtractor.util.DirectoryManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Execute tasks from a given file
 * <p>
 * executetasks <<db-directory>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Execute a list of commands from file")
public class ExecuteTasksCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "executetasks";

    // path to the database directory
    private String dbDirectoryURI;

    // absolute path to commands file
    private String pathToTasksFile;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CLICommand extractParameters() throws Exception {
        dbDirectoryURI = input.get(0);

        pathToTasksFile = DirectoryManager.getFullPath(input.get(1));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Main routine
        validateInput(2);
        extractParameters();

        // We won't execute all commands hier. It's not the purpose of this command.
        // We'll define a scheduler for this
        TaskScheduler scheduler = new TaskScheduler();


        // read file into stream
        try (Stream<String> stream = Files.lines(Paths.get(pathToTasksFile))) {

            // Each lines defines a command/task. So identify them
            stream.forEach(line -> {

                // Split the input, so we'll get each parameter as an array cell
                String[] args = line.split(" ");

                CLICommand command;

                // Command name is defined in the first cell
                // If we don't recognise it, throw an exception
                switch (args[0]) {
                    case "HTMLDumpImport":
                        command = new ImportHTMLCommand();
                        command.setInput(dbDirectoryURI, args[1]);
                        break;
                    case "CategoryLinkExtraction":
                        command = new CategoryLinksCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    case "ArticleLinkExtraction":
                        command = new ArticleLinksCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    case "EntityBaseExtraction":
                        command = new EntityExtractionCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    case "PersonExtraction":
                        command = new PersonExtractionCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    case "CityExtraction":
                        command = new CityExtractionCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    case "MonumentExtraction":
                        command = new MonumentExtractionCommand();
                        command.setInput(dbDirectoryURI);
                        break;
                    default:
                        throw new IllegalArgumentException(String.format("Unknown command: %s", args[0]));

                }

                // Schedule task
                scheduler.addTask(command);
            });

        } catch (IllegalArgumentException iae) {
            logger.error(iae.getMessage());
            logger.error(iae);
        } catch (IOException ioe) {
            logger.error("Cannot read input file {}", pathToTasksFile);
            logger.error(ioe);
        }

        // Dispatch every tasks from the queue.
        scheduler.execute();

        logger.info("{} command successfully executed!", getName());

        return null;
    }
}
