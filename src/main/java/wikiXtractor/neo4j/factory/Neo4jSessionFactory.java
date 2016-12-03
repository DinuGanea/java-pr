package wikiXtractor.neo4j.factory;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import wikiXtractor.Main;

public class Neo4jSessionFactory {

    public static final String EMBEDDED_DRIVER = "org.neo4j.ogm.drivers.embedded.driver.EmbeddedDriver";

    private final SessionFactory sessionFactory;

    //private final static SessionFactory sessionFactory = new SessionFactory(Main.DOMAIN_NAME);
    //private static Neo4jSessionFactory factory = new Neo4jSessionFactory();

    public Neo4jSessionFactory(String dbPath, String domainName) {
        this(dbPath, domainName, EMBEDDED_DRIVER);
    }


    public Neo4jSessionFactory(String dbPath, String domainName, String driverURL) {

        Configuration config = new Configuration();
        config.driverConfiguration().setDriverClassName(driverURL);
        config.set("URI", dbPath);

        sessionFactory = new SessionFactory(config, domainName);
    }

    public Session getNeo4jSession() {
        return sessionFactory.openSession();
    }
}

