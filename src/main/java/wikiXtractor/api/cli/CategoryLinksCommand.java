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

import java.util.HashMap;
import java.util.Set;

/**
 * Extract categories from each page objects and create relationships
 *
 * categorylinks <<db_directory>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Extract category links")
public class CategoryLinksCommand extends CLICommand<Void> {

    // maximal query block-size pro transaction
    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;

    // command name
    private static final String NAME = "categorylinks";

    // absolute/relative path to db directory
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

            // Start a service for db operations on category objects
            CategoryService categoryService = new CategoryService(session);

            // Keep track of all categories. We need to know which ones exist, so we create relationships on them
            // We can check if an object is contained in the map in O(1). so that's goooood
            HashMap<String, Category> catMap = categoryService.findAllCats();

            logger.info(String.format("%d category page objects loaded", catMap.size()));

            // Create a service for db operations on article objects
            ArticleService articleService = new ArticleService(session);

            // We also need to keep track of all articles so we know about their existence.
            // Allows O(1) contains operation
            HashMap<String, Article> articles = articleService.findAllArticles();

            logger.info(String.format("%d article page objects loaded", articles.size()));

            int objectsUpdated = 0;
            int relationships = 0;
            boolean updated = false;

            Category subcategory;

            // Parse each category add check for it's subcategories
            for (Category c : catMap.values()) {

                // We've pre-extracted category titles of the category to make the processing faster
                Set<String> subcategories = c.getCatTitles();

                updated = false;
                // Check if an the subcategory does exists in the db
                for (String catTitle : subcategories) {

                    // Try to extract category object from map
                    subcategory = catMap.get(catTitle);

                    // if it does exists. add it to subcategories list
                    if (subcategory != null) {
                        updated = true;
                        relationships++;
                        c.addSubcategory(subcategory);
                        logger.debug(String.format("Adding subcategory \"%s\" to category \"%s\"", catTitle, c.getPageTitle()));
                    }
                }

                // If subcategories exists, create a db query
                if (updated) {
                    categoryService.resolveParenthood(c);
                    objectsUpdated++;
                }


                logger.debug(String.format("Category \"%s\" has %d subcategories", c, c.getSubcategories().size()));

                // Push all queries to database and start a new transaction
                if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info("{} page objects updated with a total of {} relationships", objectsUpdated, relationships);
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            Category category;
            // Parse every article and assigned it to categories
            for (Article a : articles.values()) {

                updated = false;
                // Same thing as the category routine does. Parse each category and check for it's existence
                for (String catTitle : a.getCatTitles()) {

                    category = catMap.get(catTitle);

                    // If exists, add it to set
                    if (category != null) {
                        updated = true;
                        a.addCategory(category);
                        relationships++;
                        logger.debug(String.format("Adding category \"%s\" to article \"%s\"", catTitle, a.getPageTitle()));
                    }
                }

                // articles does contain categories, so create a query for relationships
                if (updated) {
                    objectsUpdated++;
                    articleService.resolveCategorisations(a);
                }

                // we've reach the maximum block-size.
                if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info("{} articles updated with a total of {} relationships", objectsUpdated, relationships);
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            // there are still queries to commit
            if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE != 0) {
                logger.info("{} articles updated with a total of {} relationships", objectsUpdated, relationships);
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
    public CategoryLinksCommand extractParameters() {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));

        return this;
    }

}
