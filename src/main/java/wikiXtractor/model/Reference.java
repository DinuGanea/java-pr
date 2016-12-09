package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Reference.TYPE)
public class Reference extends Entity {

    public static final String TYPE = "REFERS_TO";

    @StartNode
    private Article referral;

    @EndNode
    private Article referent;


    public Reference() {

    }

}

