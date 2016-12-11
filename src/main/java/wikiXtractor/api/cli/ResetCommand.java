package wikiXtractor.api.cli;

import com.beust.jcommander.Parameters;
import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.neo4j.manager.SessionManager;

/**
 * Used to clean domain metadata and initialise a new session in the given directory
 *
 * reset <<db-directory>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Reset database domain & metadata")
public class ResetCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "reset";

    // path to the database directory
    private String dbDirectoryURI;

    /**
     * {@inheritDoc}
     */
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Main routine
        validateInput(1);
        extractParameters();


        SessionManager.cleanSessionDomain(dbDirectoryURI);

        // Reset session
        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        sessionManager.addConstraints()
                .addIndexes();

        logger.info("{} command successfully executed!", getName());

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResetCommand extractParameters() {

        dbDirectoryURI = input.get(0);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return NAME;
    }
}
