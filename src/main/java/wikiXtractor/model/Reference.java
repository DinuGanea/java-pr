package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "REFER_TO")
public class Reference extends Entity {

    @StartNode
    private Page page;

    @EndNode
    private Page referent;


    public Reference() {

    }

}

