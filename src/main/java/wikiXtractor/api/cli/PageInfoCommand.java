package wikiXtractor.api.cli;


import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.model.Article;
import wikiXtractor.model.Category;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.service.ArticleService;
import wikiXtractor.util.DirectoryManager;

import java.util.Set;

public class PageInfoCommand extends CLICommand<Void> {

    private static final String NAME = "pageinfo";

    private String dbDirectoryURI;

    private int namespaceID;

    private String pageTitle;

    private Set<Category> categories;
    private Set<Article> referals;
    private Set<Article> referers;

    public Void execute() throws InvalidCLIInputException {

        validateInput();
        extractParameters();

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {

            ArticleService articleService = new ArticleService(session);

            Article article = articleService.find(namespaceID, pageTitle);

            if (article != null) {

                logger.info("Page ID: {}", article.getPageID());
                logger.info("Namespace ID: {}", article.getNamespaceID());
                logger.info("Page Title: {}", article.getPageTitle());


                logger.info("Direct Categories:");
                for (Category category : article.getCategories()) {
                    logger.info(category.getPageTitle());
                    categories.add(category);
                }

                logger.info("Indirect Categories:");
                for (Category category : article.getCategories()) {
                    if (category.getSubcategories().size() > 0) {
                        category.getSubcategories().forEach(sub -> {;
                            if (!categories.contains(sub)) {
                                displayIndirectCategories(sub);
                            }
                        });
                    }
                }


                logger.info("Referals:");
                for (Article referals :article.getReferals()) {
                    logger.info(referals.getPageTitle());
                }


            }

        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        }

        return null;
    }



    public void displayIndirectCategories(Category category) {
        logger.info(category.getPageTitle());
        if (category.getSubcategories().size() > 0) {
            category.getSubcategories().forEach(sub -> {;
                if (!categories.contains(sub)) {
                    displayIndirectCategories(sub);
                }
            });
        }
    }

    public String getName() {
        return NAME;
    }


    public PageInfoCommand validateInput() throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != 3) {
            throw new InvalidCLIInputException(String.format("Too many parameter given for \"%s\" command!", getName()));
        }

        return this;
    }


    public PageInfoCommand extractParameters() {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));
        namespaceID = Integer.parseInt(input.get(1));
        pageTitle = input.get(2);

        return this;
    }

}
