package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "HAS_CATEGORY")
public class Categorisation extends Entity {

    /**
     * The OGM also requires an public no-args constructor to be able to construct objects,
     * we’ll make sure all our entities have one.
     */
    public Categorisation() {

    }

    @StartNode
    private Page page;

    @EndNode
    private Category category;

}
