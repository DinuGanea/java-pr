package wikiXtractor.neo4j.manager;



import wikiXtractor.model.Entity;
import wikiXtractor.neo4j.factory.Neo4jSessionFactory;
import wikiXtractor.neo4j.session.Session;
import wikiXtractor.util.DirectoryManager;
import wikiXtractor.util.Loggable;

import java.util.Collections;
import java.util.HashMap;

public class SessionManager implements Loggable {

    protected Session session;

    protected String uri;
    protected String domain;


    public SessionManager(String uri, String domain) {
        this.uri = uri;
        this.domain = domain;

        openSession();
    }


    protected void openSession() {
        String fullPath = DirectoryManager.getFullPath(uri);
        String urlLikePath = DirectoryManager.fullPathToURL(fullPath);

        Neo4jSessionFactory sessionFactory = new Neo4jSessionFactory(urlLikePath, domain);

        session = sessionFactory.getNeo4jSession();

        addConstraints();
        addIndexes();

        logger.info(String.format("Openning session in %s", fullPath));
    }


    public void cleanSessionDomain() {
        DirectoryManager.cleanDir(uri);
    }


    public SessionManager resetSessionDomain() {
        cleanSessionDomain();
        openSession();

        return this;
    }


    public SessionManager addConstraints() {

        session.query(String.format("CREATE CONSTRAINT ON (p:Page) ASSERT p.%s IS UNIQUE", Entity.CUSTOM_ID_PROP_NAME), new HashMap<String, String>());

        return this;
    }


    public SessionManager addIndexes() {

        session.query("CREATE INDEX ON :Page(namespaceID)", new HashMap<String, String>());
        session.query("CREATE INDEX ON :Category(namespaceID)", new HashMap<String, String>());
        session.query("CREATE INDEX ON :Article(namespaceID)", new HashMap<String, String>());
        session.query(String.format("CREATE INDEX ON :Category(%s)", Entity.CUSTOM_ID_PROP_NAME), Collections.emptyMap());
        session.query(String.format("CREATE INDEX ON :Article(%s)", Entity.CUSTOM_ID_PROP_NAME), Collections.emptyMap());

        return this;
    }

    public Session getSession() {
        return session;
    }


}
