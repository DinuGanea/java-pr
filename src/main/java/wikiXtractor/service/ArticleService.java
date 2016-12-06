package wikiXtractor.service;

import org.neo4j.ogm.session.Session;
import wikiXtractor.model.Article;


public abstract class ArticleService extends PageService<Article> {

    public ArticleService(Session session) throws Exception {
        super(session, Article.class);
    }
}

