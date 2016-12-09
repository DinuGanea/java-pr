package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;

import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.factory.PageFactory;
import wikiXtractor.model.Page;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.service.PageService;
import wikiXtractor.util.DirectoryManager;

import java.nio.file.Paths;
import java.util.Set;

@Parameters(commandDescription = "Import HTML dump file into database")
public class ImportHTMLCommand extends CLICommand<Void> {

    private static final String NAME = "importhtml";

    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;


    private String dbDirectoryURI;
    private String htmlFileURI;


    public Void execute() throws Exception {

        validateInput();
        extractParameters();

        Set<Page> pages = PageFactory.build(Paths.get(htmlFileURI));

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        PageService<Page> service = new PageService<>(session, Page.class);

        session.beginTransaction();

        int objectsInserted = 0;
        for (Page page : pages) {
            service.createOrUpdate(page);
            objectsInserted++;

            if (objectsInserted % ImportHTMLCommand.BATCH_INSERT_BLOCK_SIZE == 0) {
                logger.info(String.format("%d page objects inserted!", objectsInserted));
                session.getTransaction().commit();
                session.beginTransaction();
            }
        }

        if (objectsInserted % ImportHTMLCommand.BATCH_INSERT_BLOCK_SIZE != 0) {
            logger.info(String.format("%d page objects inserted!", objectsInserted));
            session.getTransaction().commit();
        }


        return null;
    }



    public String getName() {
        return NAME;
    }

    public ImportHTMLCommand validateInput() throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != 2) {
            throw new InvalidCLIInputException(String.format("Too many parameter given for \"%s\" command!", getName()));
        }

        return this;
    }

    public ImportHTMLCommand extractParameters() {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));
        htmlFileURI = DirectoryManager.getFullPath(input.get(1));

        return this;
    }
}
