package wikiXtractor.service;


import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import wikiXtractor.model.*;
import wikiXtractor.util.Loggable;

import java.util.*;

/**
 * Specific implementation of the page service
 *
 * @author Ionut Urs
 */
public class ArticleService extends PageService<Article> implements Loggable {

    /**
     * {@inheritDoc}
     */
    public ArticleService(Session session) throws Exception {
        super(session, Article.class);
    }

    /**
     * Find article by name. We know it's namespaceID so we're safe
     *
     * @param name Article name
     * @return Article object
     */
    public Article find(String name) {
        return super.find(Article.NAMESPACE_ID, name);
    }

    /**
     * TODO rewrite embedded ogm driver
     *
     * Add a single reference between to article object
     *
     * @param startNode Graph ID of the referrer
     * @param endNode Graph ID of the referral
     */
    public void addReference(Long startNode, Long endNode) {
        String query = String.format("MATCH (startNode) WHERE ID(startNode) = %s MATCH (endNode) WHERE ID(endNode) = %s " +
                        "CREATE (startNode)-[rel:`%s`]->(endNode)"
                , startNode
                , endNode
                , Reference.TYPE);


        // don't worry, i know what i'm doing
        session.query(query, Collections.emptyMap());
    }


    /**
     * TODO rewrite embedded ogm driver
     * <p>
     * Extract all article objects from DB
     *
     * @return HashMap whose key is article name and value - the object itself
     */
    public HashMap<String, Article> findAllArticles() {

        // Build query
        String query = String.format("MATCH (a:Article{%s:%d}) return a",
                Page.NS_EL_NAME,
                Article.NAMESPACE_ID
        );

        // Execute and extract results
        Result result = session.query(query, Collections.emptyMap());

        HashMap<String, Article> articles = new HashMap<>();

        // Hydrate all extracted article objects to HashMap
        result.forEach(o -> {
            Article a = (Article) o.get("a");
            articles.put(a.getPageTitle(), a);
        });

        return articles;
    }

    /**
     * TODO rewrite embedded ogm driver
     * <p>
     * Create relationship between the article object and it's categories
     * <p>
     * Bad implementation, but without this, performance is really shitty. We could've use the upsert method
     * from the service, but the embedded driver is reeeeaaaly slow. We'll have to rewrite it for more performance.
     *
     * @param article Article object
     */
    public void resolveCategorisations(Article article) {

        StringBuilder query = new StringBuilder();
        StringBuilder relationships = new StringBuilder();

        int i = 0;
        for (Category category : article.getCategories()) {
            // Match all category nodes (these will be the start nodes)
            query.append(String.format("MATCH (startNode_%d) WHERE ID(startNode_%d) = %s\n", i, i, category.getId()));
            // Crate relationship query
            relationships.append(String.format("CREATE (startNode_%d)-[:`%s`]->(endNode)\n", i, Categorisation.TYPE));
            i++;
        }

        // Match the article object (The relationship is OUTGOING, so this is the end node)
        query.append(String.format("MATCH (endNode) WHERE ID(endNode) = %s\n", article.getId()));

        // Append relationship queries to the match queries
        query.append(relationships.toString());

        // FIRE!
        session.query(query.toString(), Collections.emptyMap());
    }


    /**
     * TODO rewrite embedded ogm driver
     * <p>
     * Create relationship between the article object and it's referals
     * <p>
     * Bad implementation, but without this, performance is really shitty. We could've use the upsert method
     * from the service, but the embedded driver is reeeeaaaly slow. We'll have to rewrite it for more performance.
     *
     *
     * @param article Article object
     */
    public void resolveReferences(Article article) {

        StringBuilder query = new StringBuilder();
        StringBuilder relationships = new StringBuilder();

        // Match the referrer node
        query.append(String.format("MATCH (startNode) WHERE ID(startNode) = %s\n", article.getId()));

        int i = 0;
        for (Article referal : article.getReferrals()) {
            // Match all referrals
            query.append(String.format("MATCH (endNode_%d) WHERE ID(endNode_%d) = %s\n", i, i, referal.getId()));
            // Create the relationship query
            relationships.append(String.format("CREATE (startNode)-[:`%s`]->(endNode_%d)\n", Reference.TYPE, i));
            i++;
        }

        // Append relationship queries to the match queries
        query.append(relationships.toString());

        session.query(query.toString(), Collections.emptyMap());
    }


    /**
     * TODO rewrite embedded ogm driver
     * <p>
     * Get all indirect categories pointing to this article
     * <p>
     * Bad implementation, but without this, performance is really shitty. We could've use the upsert method
     * from the service, but the embedded driver is reeeeaaaly slow. We'll have to rewrite it for more performance.
     *
     *
     * @param article Article object
     * @return LinkedHashMap, whose key is the article name, and the value the object itself
     */
    public LinkedHashMap<String, Category> getIndirectCategories(Article article) {
        LinkedHashMap<String, Category> categories = new LinkedHashMap<>();

        String query = String.format("MATCH (c:Category)-[:`%s`*]->(c1:Category)-[:`%s`]->(a:Article) WHERE ID(a) = %s return c",
                Parenthood.TYPE,
                Categorisation.TYPE,
                article.getId()
        );

        Result result = session.query(query, Collections.emptyMap());

        // Hydrate results to the LinkedHashMap object
        result.forEach(entry -> {
            Category c = (Category) entry.get("c");
            categories.put(c.getPageTitle(), c);
        });


        return categories;
    }


    /**
     * Get all articles that link to given one
     *
     * @param article Article object
     * @return LinkedHashMap, whose key is the article name, and the value the object itself
     */
    public LinkedHashMap<String, Article> getReferrers(Article article) {
        LinkedHashMap<String, Article> referrers = new LinkedHashMap<>();

        String query = String.format("MATCH (article:Article)-[:`%s`]->(a:Article) WHERE ID(a) = %s return article",
                Reference.TYPE,
                article.getId()
        );

        Result result = session.query(query, Collections.emptyMap());

        // Hydrate results to the LinkedHashMap object
        result.forEach(entry -> {
            Article a = (Article) entry.get("article");
            referrers.put(a.getPageTitle(), a);
        });

        return referrers;
    }


    /**
     * Get all articles the given article links to
     *
     * @param article Article objects
     * @return LinkedHashMap, whose key is the article name, and the value the object itself
     */
    public LinkedHashMap<String, Article> getReferrals(Article article) {
        LinkedHashMap<String, Article> referrals = new LinkedHashMap<>();

        String query = String.format("MATCH (article:Article)-[:`%s`]->(a:Article) WHERE ID(article) = %s return a",
                Reference.TYPE,
                article.getId()
        );

        Result result = session.query(query, Collections.emptyMap());

        // Hydrate results to the LinkedHashMap object
        result.forEach(entry -> {
            Article a = (Article) entry.get("a");
            referrals.put(a.getPageTitle(), a);
        });

        return referrals;
    }
}

