package wikiXtractor.api.cli;



import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.model.Article;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.service.ArticleService;
import wikiXtractor.service.CategoryService;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.Set;

public class CategoryLinksCommand extends CLICommand<Void> {

    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;

    private static final String NAME = "categorylinks";

    private String dbDirectoryURI;

    public Void execute() throws InvalidCLIInputException {

        validateInput();
        extractParameters();

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {



            CategoryService categoryService = new CategoryService(session);

            HashMap<String, Category> catMap = categoryService.findAllCats();

            logger.info(String.format("%d category page objects loaded", catMap.size()));


            ArticleService articleService = new ArticleService(session);
            HashMap<String, Article> articles = articleService.findAllArticles();

            logger.info(String.format("%d article page objects loaded", articles.size()));

            int objectsUpdated = 0;
            boolean updated = false;

            /*Category subcategory;
            for (Category c : catMap.values()) {

                Set<String> subcategories = c.getCatTitles();

                updated = false;
                for (String catTitle : subcategories) {

                    subcategory = catMap.get(catTitle);

                    if (subcategory != null) {
                        updated = true;
                        categoryService.addParenthood(c.getId(), subcategory.getId());
                        logger.debug(String.format("Adding subcategory \"%s\" to category \"%s\"", catTitle, c.getPageTitle()));
                    }
                }

                if (updated) {
                    objectsUpdated++;
                }


                logger.debug(String.format("Category \"%s\" has %d subcategories", c, c.getSubcategories().size()));

                if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info("{} page objects updated", objectsUpdated);
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }*/

            Category category;
            for (Article a : articles.values()) {

                updated = false;
                for (String catTitle : a.getCatTitles()) {

                    category = catMap.get(catTitle);

                    if (category != null) {
                        updated = true;
                        articleService.addCategorisation(category.getId(), a.getId());
                        logger.debug(String.format("Adding category \"%s\" to article \"%s\"", catTitle, a.getPageTitle()));
                    }
                }

                if (updated) {
                    objectsUpdated++;
                }

                if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE == 0) {
                    logger.info(String.format("%d page objects updated", objectsUpdated));
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            if (objectsUpdated % BATCH_INSERT_BLOCK_SIZE != 0) {
                logger.info(String.format("%d page objects updated", objectsUpdated));
                session.getTransaction().commit();
            }


        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }

    public String getName() {
        return NAME;
    }

    public CategoryLinksCommand validateInput() throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != 1) {
            throw new InvalidCLIInputException(String.format("Too many parameter given for \"%s\" command!", getName()));
        }

        return this;
    }

    public CategoryLinksCommand extractParameters() {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));

        return this;
    }

}
