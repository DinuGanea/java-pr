package wikiXtractor;

import com.beust.jcommander.JCommander;
import org.reflections.Reflections;
import wikiXtractor.api.cli.CLICommand;
import wikiXtractor.util.Loggable;

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

    // Domain of the project. Used for DB metadata and reflection
    public static final String DOMAIN_NAME = Main.class.getPackage().getName();



    public static void main(String args[]) throws Exception {
        long startTime = System.currentTimeMillis();

        JCommander commander = new JCommander();
        try {
            // Parse all classes of the given package
            Reflections reflections = new Reflections(DOMAIN_NAME);
            // Load classes that are extending from the CLICommand class (we need them for the parser)
            Set<Class<? extends CLICommand>> commands = reflections.getSubTypesOf(CLICommand.class);

            for (Class cls : commands) {
                // Create a new instance for the command class
                CLICommand command = (CLICommand) cls.newInstance();
                // Add it to the listener
                commander.addCommand(command.getName(), command);
            }

            // Parse the input
            commander.parse(args);

            // Identify entered command
            String parsedCommand = commander.getParsedCommand();
            // Extact instance of the entered command
            CLICommand commandObject = (CLICommand) commander.getCommands().get(parsedCommand).getObjects().get(0);

            // Do the routine
            commandObject.execute();

        } catch (NullPointerException npe) {

            logger.error("No command entered. Please check the usage:");
            commander.usage();


        } catch (Exception e) {

            logger.error("Oooops an Exception occured! {}", e.getMessage());
            logger.error(e);
        }

        logger.info("Runtime {} sec.", Math.ceil((System.currentTimeMillis() - startTime) / 1000));
    }
}
