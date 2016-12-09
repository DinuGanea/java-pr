package wikiXtractor.api.cli;

import com.beust.jcommander.Parameters;
import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.neo4j.manager.SessionManager;

@Parameters(commandDescription = "Reset database files")
public class ResetCommand extends CLICommand<Void> {

    private static final String NAME = "reset";

    private String dbDirectoryURI;

    public Void execute() throws Exception {

        validateInput();
        extractParameters();

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        sessionManager.resetSessionDomain();

        return null;
    }


    public ResetCommand validateInput() throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != 1) {
            throw new InvalidCLIInputException(String.format("Too many parameter given for \"%s\" command!", getName()));
        }

        return this;
    }

    public ResetCommand extractParameters() {

        dbDirectoryURI = input.get(0);

        return this;
    }

    public String getName() {
        return NAME;
    }
}
