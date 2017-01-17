package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.factory.PageFactory;
import wikiXtractor.model.Page;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.PageService;
import wikiXtractor.util.DirectoryManager;

import java.nio.file.Paths;
import java.util.Set;

/**
 * Import dump html file to database
 *
 * importhtml <<db_directory>> <<path_to_html_file>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Import HTML dump file into database")
public class ImportHTMLCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "createdb";

    // maximal query block-size pro transaction
    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;

    // absolute/relative path to db directory
    private String dbDirectoryURI;

    // absolute/relative path to html file
    private String htmlFileURI;

    /**
     * {@inheritDoc}
     */
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Default routine
        validateInput(2);
        extractParameters();

        // Get pages from factory
        Set<Page> pages = PageFactory.build(Paths.get(htmlFileURI));

        // Reset session domain
        SessionManager.cleanSessionDomain(dbDirectoryURI);

        // Init session
        SessionManager sessionManager = SessionManager.getInstance(dbDirectoryURI, Main.DOMAIN_NAME);
        sessionManager.addConstraints().addIndexes();

        Session session = sessionManager.getSession();

        // Start a service for db operations on db page
        PageService<Page> service = new PageService<>(session, Page.class);

        session.beginTransaction();
        try {
            // Parse every page object and save it to database. Keep track of objects number for logging and trasaction
            int objectsInserted = 0;
            for (Page page : pages) {
                service.upsert(page);
                objectsInserted++;

                // Max block-size reached. Commit and start a new trasaction
                if (objectsInserted % ImportHTMLCommand.BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info(String.format("%d page objects inserted!", objectsInserted));
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            // If some objects still need to be commited
            if (objectsInserted % ImportHTMLCommand.BATCH_INSERT_BLOCK_SIZE != 0) {
                logger.info(String.format("%d page objects inserted!", objectsInserted));
                session.getTransaction().commit();
            }

            if (session.getTransaction() != null) {
                session.getTransaction().close();
            }


        } catch (Exception e) {
            if (session.getTransaction().status() != Transaction.Status.OPEN) {
                session.getTransaction().rollback();
            }
            throw e;
        }

        logger.info("{} command successfully executed!", getName());

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public String getName() {
        return NAME;
    }


    /**
     * {@inheritDoc}
     */
    public ImportHTMLCommand extractParameters() throws Exception {

        dbDirectoryURI = DirectoryManager.createFullPathTo(input.get(0));
        htmlFileURI = DirectoryManager.createFullPathTo(input.get(1));

        return this;
    }
}
