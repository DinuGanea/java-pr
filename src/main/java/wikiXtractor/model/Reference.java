package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Entity for reference relationship
 *
 * @author Delara Nasri
 */
@RelationshipEntity(type = Reference.TYPE)
public class Reference extends Entity {

    public static final String TYPE = "REFERS_TO";

    @StartNode
    private Entity referral;

    @EndNode
    private Entity referrer;

    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public Reference() {

    }

    public void setStartNode(Article a) {
        this.referrer = a;
    }


    public void setEndNode(Article a) {
        this.referral = a;
    }

    /**
     * Get referral article
     *
     * @return Referral article object
     */
    public Entity getEndNode() {
        return referral;
    }

    /**
     * Get referrer
     *
     * @return Referrer article object
     */
    public Entity getStartNode() {
        return referrer;
    }

}

