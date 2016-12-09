package wikiXtractor;

import com.beust.jcommander.JCommander;
import org.neo4j.ogm.session.Session;
import org.reflections.Reflections;
import wikiXtractor.api.cli.CLICommand;
import wikiXtractor.model.Article;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.ArticleService;
import wikiXtractor.service.CategoryService;
import wikiXtractor.util.Loggable;

import java.util.Objects;
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

    public static final String DOMAIN_NAME = Main.class.getPackage().getName();

    public static void main(String args[]) throws Exception {


        try {
            JCommander commander = new JCommander();


            Reflections reflections = new Reflections(DOMAIN_NAME);
            Set<Class<? extends CLICommand>> commands = reflections.getSubTypesOf(CLICommand.class);

            for (Class cls : commands) {
                CLICommand command = (CLICommand) cls.newInstance();

                commander.addCommand(command.getName(), command);
            }

            commander.parse(args);

            String parsedCommand = commander.getParsedCommand();
            CLICommand commandObject = (CLICommand) commander.getCommands().get(parsedCommand).getObjects().get(0);

            commandObject.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
