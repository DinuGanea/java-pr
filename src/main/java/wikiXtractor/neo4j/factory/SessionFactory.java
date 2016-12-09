package wikiXtractor.neo4j.factory;


import org.neo4j.ogm.MetaData;
import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.service.Components;
import wikiXtractor.neo4j.session.Session;

public class SessionFactory extends org.neo4j.ogm.session.SessionFactory {


    public SessionFactory(Configuration configuration, String... packages) {
        super(configuration, packages);
    }

    public Session openSession() {
        return new Session(metaData(), Components.driver());
    }

}
