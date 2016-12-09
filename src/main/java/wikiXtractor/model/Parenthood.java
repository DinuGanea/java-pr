package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Parenthood.TYPE)
public class Parenthood extends Entity {

    public static final String TYPE = "PARENT_OF";

    /**
     * The OGM also requires an public no-args constructor to be able to construct objects,
     * we’ll make sure all our entities have one.
     */
    public Parenthood() {

    }

    @StartNode
    private Category parent;

    @EndNode
    private Category child;


    public Parenthood setStartNode(Category parent) {
        this.parent = parent;
        return this;
    }

    public Parenthood setEndNode(Category child) {
        this.child = child;
        return this;
    }

    public Category getParent() {
        return parent;
    }

    public Category getChild() {
        return child;
    }


}
