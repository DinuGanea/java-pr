package wikiXtractor.api.cli;

import wikiXtractor.Main;
import wikiXtractor.api.cli.exceptions.InvalidCLIInputException;
import wikiXtractor.extractors.LinkExtraction;
import wikiXtractor.factory.PageFactory;
import wikiXtractor.model.Article;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.service.ArticleService;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.Set;


public class ArticleLinksCommand extends CLICommand<Void> {

    private static final int BATCH_INSERT_BLOCK_SIZE = 2000;

    private static final String NAME = "articlelinks";

    private String dbDirectoryURI;

    public Void execute() throws InvalidCLIInputException {

        validateInput();
        extractParameters();

        SessionManager sessionManager = new SessionManager(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        session.beginTransaction();

        try {


            ArticleService articleService = new ArticleService(session);
            HashMap<String, Article> articles = articleService.findAllArticles();

            logger.info(String.format("%d article page objects loaded", articles.size()));

            int objectsUpdated = 0;
            boolean updated = false;

            int i = 0;
            for (Article a : articles.values()) {

                Set<String> articleLinks = LinkExtraction.extractWikiLinks(a.getHtmlContent(), a.getPageTitle());

                for (String link : articleLinks) {

                    Article referal = articles.get(link);

                    if (referal != null) {
                        updated = true;
                        articleService.addReference(a.getId(), referal.getId());
                        logger.debug(String.format("Adding reference from \"%s\" to \"%s\"", referal.getPageTitle(), referal.getPageTitle()));
                    }
                }

                logger.info("{} page objects parsed", i++);


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

    public ArticleLinksCommand validateInput() throws InvalidCLIInputException {

        if (input == null) {
            throw new InvalidCLIInputException(String.format("No input found for \"%s\" command!", getName()));
        }

        if (input.size() != 1) {
            throw new InvalidCLIInputException(String.format("Too many parameter given for \"%s\" command!", getName()));
        }

        return this;
    }

    public ArticleLinksCommand extractParameters() {

        dbDirectoryURI = DirectoryManager.getFullPath(input.get(0));

        return this;
    }


}