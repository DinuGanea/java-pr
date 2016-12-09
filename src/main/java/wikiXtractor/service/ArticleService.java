package wikiXtractor.service;


import org.neo4j.ogm.model.Result;
import wikiXtractor.model.*;
import wikiXtractor.neo4j.session.Session;

import java.util.Collections;
import java.util.HashMap;


public class ArticleService extends PageService<Article> {

    public ArticleService(Session session) throws Exception {
        super(session, Article.class);
    }

    public Article find(String name) {
        return super.find(Article.NAMESPACE_ID, name);
    }

    public HashMap<String, Article> findAllArticles() {
        String query = String.format("" +
                        "MATCH (p:Article{%s:%d}) return p",
                "namespaceID",
                0
        );


        Result result = session.query(query, Collections.emptyMap());


        HashMap<String, Article> articles = new HashMap<>();

        result.forEach(o -> {
            Article p = (Article) o.get("p");
            articles.put(p.getPageTitle(), p);
        });


        //System.out.println(String.format("%d categories found", i));
        //result.forEach(System.out::println);


        return articles;
    }

    // Bad implementation, but without this, performance is really shitty
    public Result addCategorisation(Long startNode, Long endNode) {
        String query = String.format("" +
                        "MATCH (startNode) WHERE ID(startNode) = %s MATCH (endNode) WHERE ID(endNode) = %s CREATE (startNode)-[rel:`%s`]->(endNode)"
                , startNode
                , endNode
                , Categorisation.TYPE);


        return session.query(query, Collections.emptyMap());
    }


    public Result addReference(Long startNode, Long endNode) {
        String query = String.format("" +
                        "MATCH (startNode) WHERE ID(startNode) = %s MATCH (endNode) WHERE ID(endNode) = %s CREATE (startNode)-[rel:`%s`]->(endNode)"
                , startNode
                , endNode
                , Reference.TYPE);


        return session.query(query, Collections.emptyMap());
    }
}

