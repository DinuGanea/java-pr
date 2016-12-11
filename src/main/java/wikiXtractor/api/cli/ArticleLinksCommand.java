package wikiXtractor.api.cli;

import com.beust.jcommander.Parameters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.extractors.LinkExtraction;
import wikiXtractor.model.Article;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.ArticleService;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.Set;

/**
 * Extract links between articles
 *
 * articlelinks <<db_directory>>
 *
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Extract links between articles")
public class ArticleLinksCommand extends CLICommand<Void> {

    // maximal query block-size pro transaction
    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;

    // command name
    private static final String NAME = "articlelinks";

    // path to directory path
    private String dbDirectoryURI;

    /**
     * {@inheritDoc}
     */
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Default routine
        validateInput(1);
        extractParameters();

        // Init session
        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {

            // Create a service to execute db operations
            ArticleService articleService = new ArticleService(session);

            // Hash map allows existence check operation in O(1) , so that comes in handy
            HashMap<String, Article> articles = articleService.findAllArticles();

            logger.info(String.format("%d article page objects loaded", articles.size()));

            int objectsUpdated = 0;
            boolean updated = false;

            int relationships = 0;

            // Parse every article and extract article links
            for (Article a : articles.values()) {

                // Extract article links at this point
                Set<String> articleLinks = LinkExtraction.extractWikiLinks(a.getHtmlContent(), a.getPageTitle());

                updated = false;
                // For each links, check existence of the article
                for (String link : articleLinks) {

                    Article referral = articles.get(link);

                    // Object exists, add the reference to it
                    if (referral != null) {
                        updated = true;
                        a.addReferral(referral);
                        relationships++;
                        //articleService.addReference(a.getId(), referral.getId());
                        logger.debug(String.format("Adding reference from \"%s\" to \"%s\"", a.getPageTitle(), referral.getPageTitle()));
                    }
                }

                // We found some references, so create the query
                if (updated) {
                    //articleService.resolveReferences(a);
                    articleService.upsert(a);
                    objectsUpdated++;
                }

                // Max block-size reached
                if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info("{} page objects updated with a total of {} relationshiops", objectsUpdated, relationships);
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            // There are still queries to be executed
            if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE != 0) {
                logger.info("{} page objects updated with a total of {} relationshiops", objectsUpdated, relationships);
                session.getTransaction().commit();
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
    public ArticleLinksCommand extractParameters() throws Exception {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));

        return this;
    }


}