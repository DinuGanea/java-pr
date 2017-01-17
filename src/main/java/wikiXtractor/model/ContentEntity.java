package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity for a City object
 *
 * @author Delara Nasri
 */
@NodeEntity
public abstract class ContentEntity extends Entity {

    public static final int MONUMENT_NODE = 1;
    public static final int CITY_NODE = 2;
    public static final int PERSON_NODE = 3;

    // Title that is assigned to the entity after extraction
    private String rawTitle;

    // Article that identifies this entity
    @Relationship(type = Reference.TYPE, direction = Relationship.INCOMING)
    private Page sourcePage;

    // Monument nodes, that refer to this object
    @Relationship(type = Reference.TYPE, direction = Relationship.INCOMING)
    private Set<ContentEntity> monumentReferrers = new HashSet<>();


    // Person nodes, that refer to this object
    @Relationship(type = Reference.TYPE, direction = Relationship.INCOMING)
    private Set<ContentEntity> personReferrers = new HashSet<>();

    // City nodes, that refer to this object
    @Relationship(type = Reference.TYPE, direction = Relationship.INCOMING)
    private Set<ContentEntity> cityReferrers = new HashSet<>();


    // Monument nodes, this object refers to
    @Relationship(type = Reference.TYPE, direction = Relationship.OUTGOING)
    private Set<ContentEntity> monumentReferrals = new HashSet<>();


    // Person nodes, this object refers to
    @Relationship(type = Reference.TYPE, direction = Relationship.OUTGOING)
    private Set<ContentEntity> personReferrals = new HashSet<>();

    // City nodes, this object refers to
    @Relationship(type = Reference.TYPE, direction = Relationship.OUTGOING)
    private Set<ContentEntity> cityReferrals = new HashSet<>();

    /**
     * @param type Referrers type (defined as constant)
     * @return Set of referrers
     * @throws Exception
     */
    public Set<ContentEntity> getReferrers(int type) throws Exception {

        switch (type) {
            case MONUMENT_NODE:
                return monumentReferrers;
            case CITY_NODE:
                return cityReferrers;
            case PERSON_NODE:
                return personReferrers;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }
    }

    /**
     * Add a single referrer to the set
     *
     * @param referrer Entity object
     * @param type     Referrer type (defined as constant)
     * @return ContentEntity instance
     * @throws Exception
     */
    public ContentEntity addReferrer(ContentEntity referrer, int type) throws Exception {
        switch (type) {
            case MONUMENT_NODE:
                monumentReferrers.add(referrer);
                break;
            case CITY_NODE:
                cityReferrers.add(referrer);
                break;
            case PERSON_NODE:
                personReferrers.add(referrer);
                break;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }

        return this;
    }


    /**
     * @param referrers Set of referrers
     * @param type      Referrers type (defined as constant)
     * @return ContentEntity instance
     * @throws Exception
     */
    public ContentEntity setReferrers(Set<ContentEntity> referrers, int type) throws Exception {

        switch (type) {
            case MONUMENT_NODE:
                monumentReferrers = referrers;
                break;
            case CITY_NODE:
                cityReferrers = referrers;
                break;
            case PERSON_NODE:
                personReferrers = referrers;
                break;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }

        return this;
    }

    /**
     * @param type Referrals type (defined as constant)
     * @return Set of referrals
     * @throws Exception
     */
    public Set<ContentEntity> getReferrals(int type) throws Exception {

        switch (type) {
            case MONUMENT_NODE:
                return monumentReferrals;
            case CITY_NODE:
                return cityReferrals;
            case PERSON_NODE:
                return personReferrals;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }

    }

    /**
     * Add a single referral to the set
     *
     * @param referral Entity object
     * @param type     Referral type (defined as constant)
     * @return ContentEntity instance
     * @throws Exception
     */
    public ContentEntity addReferral(ContentEntity referral, int type) throws Exception {

        switch (type) {
            case MONUMENT_NODE:
                monumentReferrals.add(referral);
                break;
            case CITY_NODE:
                cityReferrals.add(referral);
                break;
            case PERSON_NODE:
                personReferrals.add(referral);
                break;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }

        return this;
    }

    /**
     * @param referrals Set of referrers
     * @param type      Referrals type (defined as constant)
     * @return ContentEntity instance
     * @throws Exception
     */
    public ContentEntity setReferrals(Set<ContentEntity> referrals, int type) throws Exception {

        switch (type) {
            case MONUMENT_NODE:
                monumentReferrals = referrals;
                break;
            case CITY_NODE:
                cityReferrals = referrals;
                break;
            case PERSON_NODE:
                personReferrals = referrals;
                break;
            default:
                throw new Exception(String.format("Unknown entity type %d", type));
        }

        return this;
    }

    /**
     * @return page that identifies the entity
     */
    public Page getSourcePage() {
        return sourcePage;
    }

    /**
     * @param page page that identifies the entity
     * @return ContentEntity instance
     */
    public ContentEntity setSourcePage(Page page) {
        this.sourcePage = page;
        return this;
    }

    /**
     * @return unchecked title of the entity
     */
    public String getRawTitle() {
        return rawTitle;
    }

    /**
     * @param title unchecked title of the entity
     * @return ContentEntity instance
     */
    public ContentEntity setRawTitle(String title) {
        rawTitle = title;
        return this;
    }
}
