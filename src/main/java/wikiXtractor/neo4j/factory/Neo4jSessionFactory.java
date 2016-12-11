package wikiXtractor.neo4j.factory;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;


/**
 * Handles singleton creation of session objects
 *
 * @author Ionut Urs
 */
public class Neo4jSessionFactory {

    // Connection driver. It's reeeaally slow. We need to rewrite it
    public static final String EMBEDDED_DRIVER = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";

    // Ssession factory of the OGM package
    private final SessionFactory sessionFactory;

    /**
     * Mark database local directory
     *
     * @param dbPath Path to database
     * @param domainName Metadata domain
     */
    public Neo4jSessionFactory(String dbPath, String domainName) {
        this(dbPath, domainName, EMBEDDED_DRIVER);
    }

    /**
     * Mark database location (HTTP/LOCAL...)
     *
     * @param dbPath Path to database
     * @param domainName Metadata domain
     * @param driverURL Connection driver
     */
    public Neo4jSessionFactory(String dbPath, String domainName, String driverURL) {

        // Apply configuration
        Configuration config = new Configuration();
        config.driverConfiguration().setDriverClassName(driverURL);
        config.set("URI", dbPath);

        // Create a factory
        sessionFactory = new SessionFactory(config, domainName);
    }

    /**
     * Borrow the session from it's factory
     *
     * @return DB session object
     */
    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }
}

