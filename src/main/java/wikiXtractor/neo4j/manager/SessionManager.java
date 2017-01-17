package wikiXtractor.neo4j.manager;



import org.neo4j.ogm.session.Session;
import wikiXtractor.model.ContentEntity;
import wikiXtractor.model.Entity;
import wikiXtractor.neo4j.factory.Neo4jSessionFactory;
import wikiXtractor.util.DirectoryManager;
import wikiXtractor.util.Loggable;

import java.util.Collections;
import java.util.HashMap;

/**
 * DB session manager
 *
 * @author Dinu Ganea
 */
public class SessionManager implements Loggable {

    protected static SessionManager instance = null;

    // DB session object
    protected Session session;

    // Relative path to database
    protected String uri;

    // Metadata domain
    protected String domain;

    /**
     * Starts a new manager
     * Won't allow instantiation of the session object by default. Use singleton
     *
     * @param uri Relative path to the database
     * @param domain Metadata domain
     */
    protected SessionManager(String uri, String domain) throws Exception {
        this.uri = uri;
        this.domain = domain;

    }


    /**
     * Singleton
     *
     * @param uri Relative path to the database
     * @param domain Metadata domain
     * @return SessionManager instance
     * @throws Exception
     */
    public static SessionManager getInstance(String uri, String domain) throws Exception {
        if (instance == null) {
            instance = new SessionManager(uri, domain);
            // Will create metadata only once
            instance.openSession();
        }

        return instance;
    }


    /**
     * Open local session in the given path
     *
     * @return SessionManager object
     */
    public SessionManager openSession() throws Exception {
        String fullPath = DirectoryManager.createFullPathTo(uri);
        String urlLikePath = DirectoryManager.fullPathToURL(fullPath);

        logger.info(String.format("Opening session in %s", fullPath));



        // Get the factory object. We don't want to create every time new session object
        Neo4jSessionFactory sessionFactory = new Neo4jSessionFactory(urlLikePath, domain);

        // Get the actually session object
        session = sessionFactory.getNeo4jSession();

        logger.info("Session initiated");

        return this;
    }


    /**
     * Remove session metadata and close session
     *
     * @param uri Path to DB directory
     */
    public static void cleanSessionDomain(String uri) throws Exception {
        logger.info("Cleaning session domain in {}", uri);
        DirectoryManager.cleanDir(uri);
    }

    /**
     * Apply needed constraints to the database.
     *
     * @return this
     */
    public SessionManager addConstraints() {

        logger.info("Adding constraints");

        // Unique KEY on page custom ID property
        session.query(String.format("CREATE CONSTRAINT ON (p:Page) ASSERT p.%s IS UNIQUE", Entity.CUSTOM_ID_PROP_NAME), new HashMap<String, String>());

        logger.info("Constraints added");

        return this;
    }

    /**
     * Add indexes for database. Make the db as fast as possible. But be careful, cause indexes are loaded in RAM,
     * so you wan't keep them as few as possible
     *
     * @return this
     */
    public SessionManager addIndexes() {

        logger.info("Adding indexes");

        // Index on namespaceID property of page Object
        session.query("CREATE INDEX ON :Page(namespaceID)", new HashMap<String, String>());

        /**
         * Although Category and Article inherit from Page, their indexes are separately loaded in another map in RAM
         */

        // Index on namespaceID property of Category object
        session.query("CREATE INDEX ON :Category(namespaceID)", new HashMap<String, String>());
        // Index on namespaceID property of Article object
        session.query("CREATE INDEX ON :Article(namespaceID)", new HashMap<String, String>());

        // Index on customID property of Category object
        session.query(String.format("CREATE INDEX ON :Category(%s)", Entity.CUSTOM_ID_PROP_NAME), Collections.emptyMap());
        // Index on customID property of Article object
        session.query(String.format("CREATE INDEX ON :Article(%s)", Entity.CUSTOM_ID_PROP_NAME), Collections.emptyMap());

        // Index on rawTitle property of Person object
        session.query(String.format("CREATE INDEX ON :Person(%s)", ContentEntity.RAW_TITLE_KEY), Collections.emptyMap());
        // Index on rawTitle property of City object
        session.query(String.format("CREATE INDEX ON :City(%s)", ContentEntity.RAW_TITLE_KEY), Collections.emptyMap());
        // Index on rawTitle property of Monument object
        session.query(String.format("CREATE INDEX ON :Monument(%s)", ContentEntity.RAW_TITLE_KEY), Collections.emptyMap());

        logger.info("Indexes added");

        return this;
    }


    /**
     * Get the session
     *
     * @return DB Session object
     */
    public Session getSession() {
        return session;
    }


}
