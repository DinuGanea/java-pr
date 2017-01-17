package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.model.*;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.ArticleService;
import wikiXtractor.service.ContentEntityService;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Extract every object from a page into a separate node
 * <p>
 * EntityBaseExtraction <<db-directory>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Extract every object from a page into a separate node")
public class EntityExtractionCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "EntityBaseExtraction";

    // absolute/relative path to db directory
    private String dbDirectoryURI;

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

        dbDirectoryURI = DirectoryManager.createFullPathTo(input.get(0));

        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Default routine
        validateInput(1);
        extractParameters();


        // Init session
        SessionManager sessionManager = SessionManager.getInstance(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        int extractedPersons = 0;
        int extractedMonuments = 0;
        int extractedCities = 0;

        try {

            // Create a service to execute db operations
            ArticleService articleService = new ArticleService(session);

            ContentEntityService<ContentEntity> contentEntityService = new ContentEntityService<>(session, ContentEntity.class);

            // Hash map allows existence check operation in O(1) , so that comes in handy
            HashMap<String, Article> articles = articleService.findAllArticles();

            logger.info(String.format("%d article page objects loaded", articles.size()));


            // Parse every article and extract article links
            for (Article a : articles.values()) {

                Set<String> categories = a.getCatTitles();

                ContentEntity contentEntity = null;

                // Identify page as an object, by parsing each category
                for (String c : categories) {
                    if (City.matchesCategory(c)) {
                        contentEntity = new City();
                        extractedCities++;
                        break;
                    } else if (Person.matchesCategory(c)) {
                        contentEntity = new Person();
                        extractedPersons++;
                        break;
                    } else if (Monument.matchesCategory(c)) {
                        contentEntity = new Monument();
                        extractedMonuments++;
                        break;
                    }
                }

                // If a object was identified, add it to database
                if (contentEntity != null) {
                    contentEntity.setSourcePage(a);
                    contentEntity.setRawTitle(a.getPageTitle());
                    contentEntityService.upsert(contentEntity);

                    if (extractedCities + extractedMonuments + extractedPersons % 2000 == 0) {
                        logger.info("{} objects extracted and committed to database", extractedCities + extractedMonuments + extractedPersons);
                        session.getTransaction().commit();
                        session.beginTransaction();
                    }

                } else {
                    logger.debug("No object extracted out of the \"{}\" page", a.getPageTitle());
                }
            }

            if (extractedCities + extractedMonuments + extractedPersons % 2000 != 0) {
                session.getTransaction().commit();
            }


        } catch (Exception e) {
            if (session.getTransaction().status() != Transaction.Status.OPEN) {
                session.getTransaction().rollback();
            }
            throw e;
        }

        logger.info("{} person objects extracted!", extractedPersons);
        logger.info("{} city objects extracted!", extractedCities);
        logger.info("{} monument objects extracted!", extractedMonuments);

        logger.info("{} command successfully executed!", getName());

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public Set<String> getDependencies() {
        return new HashSet<String>() {{
            add(ImportHTMLCommand.class.getSimpleName());
        }};
    }
}
