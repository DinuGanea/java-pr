package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "REFERS_TO")
public class Reference<T> extends Entity {

    @StartNode
    private T referral;

    @EndNode
    private T referent;


    public Reference() {

    }

}

