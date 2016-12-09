package wikiXtractor.neo4j.session;


import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.context.MappingContext;
import org.neo4j.ogm.driver.Driver;
import org.neo4j.ogm.session.Neo4jSession;
import org.neo4j.ogm.session.transaction.DefaultTransactionManager;

public class Session extends Neo4jSession {

    public Session(MetaData metaData, Driver driver) {
        super(metaData, driver);
    }


    public boolean eventsEnabled() {
        return false;
    }



}
