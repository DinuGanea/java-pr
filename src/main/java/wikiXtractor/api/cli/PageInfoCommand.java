package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.model.Article;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.ArticleService;
import wikiXtractor.service.CategoryService;
import wikiXtractor.util.DirectoryManager;

import java.security.InvalidParameterException;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Get following information about a specific page object:
 * - direct categories
 * - indirect categories
 * - referral articles
 * - referrer articles
 *
 * pageinfo <<db-directory>> <<namespace_id>> <<page_title>>
 *
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Return information for the given page")
public class PageInfoCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "pageinfo";

    // path to database directory
    private String dbDirectoryURI;

    // page namespace ID
    private int namespaceID;

    // page title
    private String pageTitle;

    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        validateInput(3);
        extractParameters();

        /**
         *
         * Following properties do not apply for categries:
         *
         * Liste der direkten Kategorien
         * Liste der direkten und indirekten Kategorien (im Kategorien-Graph)
         * Liste der Artikel ausgeben auf welche die Seite verweist
         * Liste der Artikel ausgeben welche auf den betreffenden Artikel verweisen
         *
         * A category only has parent and child categories
         *
         */

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {

            if (namespaceID == Category.NAMESPACE_ID) {

                CategoryService categoryService = new CategoryService(session);
                Category category = categoryService.find(pageTitle);

                logger.info("Page ID: {}", category.getPageID());
                logger.info("Namespace ID: {}", category.getNamespaceID());
                logger.info("Page Title: {}", category.getPageTitle());

            } else if (namespaceID == Article.NAMESPACE_ID) {

                ArticleService articleService = new ArticleService(session);

                Article article = articleService.find(namespaceID, pageTitle);

                if (article != null) {

                    logger.info("Page ID: {}", article.getPageID());
                    logger.info("Namespace ID: {}", article.getNamespaceID());
                    logger.info("Page Title: {}", article.getPageTitle());


                    // Collect all information
                    LinkedHashMap<String, Category> allCategories = articleService.getIndirectCategories(article);
                    LinkedHashMap<String, Article> referrers = articleService.getReferrers(article);
                    LinkedHashMap<String, Article> referrals = articleService.getReferrals(article);

                    logger.info("\n\nDirect Categories:");
                    for (Category category : article.getCategories()) {
                        logger.info(category.getPageTitle());
                        allCategories.put(category.getPageTitle(), category);
                    }

                    logger.info("\n\nDirect & Indirect Categories:");
                    allCategories.keySet().forEach(logger::info);


                    logger.info("\n\nReferrals:");
                    referrals.keySet().forEach(logger::info);


                    logger.info("\n\nReferrers:");
                    referrers.keySet().forEach(logger::info);

                }

            } else {
                throw new InvalidParameterException(String.format("NamespaceID %d is not accepted! You only can get info about pages with namespace %d",
                        namespaceID,
                        Article.NAMESPACE_ID)
                );
            }

            logger.info("{} command successful executed!", getName());

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
    public PageInfoCommand extractParameters() throws Exception {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));
        namespaceID = Integer.parseInt(input.get(1));
        pageTitle = input.get(2);

        return this;
    }

}
