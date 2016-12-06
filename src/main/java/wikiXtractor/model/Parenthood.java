package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = Parenthood.TYPE)
public class Parenthood<T> extends Entity {

    public static final String TYPE = "PARENT_OF";

    /**
     * The OGM also requires an public no-args constructor to be able to construct objects,
     * we’ll make sure all our entities have one.
     */
    public Parenthood() {

    }

    @StartNode
    private T parent;

    @EndNode
    private T child;


    public Parenthood<T> setStartNode(T parent) {
        this.parent = parent;
        return this;
    }

    public Parenthood<T> setEndNode(T child) {
        this.child = child;
        return this;
    }

    public T getParent() {
        return parent;
    }

    public T getCategory() {
        return child;
    }


}
