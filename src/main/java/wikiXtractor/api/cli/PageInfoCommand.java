package wikiXtractor.api.cli;


import com.beust.jcommander.Parameters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.model.Article;
import wikiXtractor.model.Category;
import wikiXtractor.model.ContentEntity;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.ArticleService;
import wikiXtractor.service.ContentEntityService;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

/**
 * Get following information about a specific article object:
 * - direct categories
 * - indirect categories
 * - referral articles
 * - referrer articles
 * - entity objects linked and their properties
 * - referrers and referrals of each entity object
 *
 * queryentity <<db-directory>> <<article_title>>
 *
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Return information for the given page")
public class PageInfoCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "queryentity";

    // path to database directory
    private String dbDirectoryURI;

    // page title
    private String pageTitle;

    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        validateInput(2);
        extractParameters();

        SessionManager sessionManager = SessionManager.getInstance(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {

            ArticleService articleService = new ArticleService(session);
            ContentEntityService<ContentEntity> contentEntityService = new ContentEntityService<>(session, ContentEntity.class);

            Article article = articleService.find(Article.NAMESPACE_ID, pageTitle);

            if (article != null) {

                logger.info("Page ID: {}", article.getPageID());
                logger.info("Namespace ID: {}", article.getNamespaceID());
                logger.info("Page Title: {}", article.getPageTitle());


                // Collect all information
                LinkedHashMap<String, Category> allCategories = articleService.getIndirectCategories(article);
                LinkedHashMap<String, Article> referrers = articleService.getReferrers(article);
                LinkedHashMap<String, Article> referrals = articleService.getReferrals(article);
                LinkedHashMap<String, ContentEntity> linkedObjects = articleService.getLinkedObjects(article);

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


                logger.info("\n\nLinked Objects:");
                linkedObjects.forEach((objName, obj) -> {
                    logger.info("{} - of type {}", objName, obj.getClass().getSimpleName());
                    logger.info("Properties: \n{}", obj);

                    HashMap<String, ContentEntity> objReferrers = contentEntityService.getReferrers(obj);
                    if (objReferrers.size() > 0) {
                        logger.info("\n{} referrers:", objName);
                        objReferrers.forEach((refName, ref) -> {
                            logger.info("{} of type {}", refName, ref.getClass().getSimpleName());
                        });
                    } else {
                        logger.info("\nNo referrers found for {}", objName);
                    }


                    HashMap<String, ContentEntity> objReferrals = contentEntityService.getReferrals(obj);
                    if (objReferrals.size() > 0) {
                        logger.info("\n{} referrals:", objName);
                        objReferrers.forEach((refName, ref) -> {
                            logger.info("{} of type {}", refName, ref.getClass().getSimpleName());
                        });
                    } else {
                        logger.info("\nNo referrals found for {}", objName);
                    }

                });

                logger.info("\n\n");
            }

        } catch (Exception e) {

            e.printStackTrace();
            System.exit(1);

            if (session.getTransaction().status() != Transaction.Status.OPEN) {
                session.getTransaction().rollback();
            }
            throw e;
        } finally {
            logger.info("{} command successfully executed!", getName());
        }


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

        dbDirectoryURI = DirectoryManager.createFullPathTo(input.get(0));
        pageTitle = input.get(1);

        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getDependencies() {
        return new HashSet<String>() {{
            add(ImportHTMLCommand.class.getSimpleName());
            add(EntityExtractionCommand.class.getSimpleName());
            add(CategoryLinksCommand.class.getSimpleName());
            add(ArticleLinksCommand.class.getSimpleName());
            add(MonumentExtractionCommand.class.getSimpleName());
            add(PersonExtractionCommand.class.getSimpleName());
            add(CityExtractionCommand.class.getSimpleName());
        }};
    }

}
