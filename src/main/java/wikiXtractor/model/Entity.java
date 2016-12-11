package wikiXtractor.model;


/**
 * Entity object that represents nodes/relationships in DB
 *
 * @author Delara Nasri
 */
public abstract class Entity {

    // Name of the custom ID column
    public static final String CUSTOM_ID_PROP_NAME = "customID";

    // Graph ID. Assigned by Neo4j when inserting a new object in DB, so we don't need any setter for it.
    private Long id;

    // ID defined from the entity. Makes the search easier
    protected String customID;

    /**
     * Get custom entity identifier
     *
     * @return Entity identififer
     */
    public String getCustomID() {
        return customID;
    }

    /**
     * Get id assigned to entity by Neo4j
     *
     * @return Entity identifier
     */
    public Long getId() {
        return id;
    }


    /**
     *
     * @param o Object to compare towards
     * @return True if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || id == null || getClass() != o.getClass()) {
            return false;
        }

        Entity entity = (Entity) o;

        return id.equals(entity.id);
    }

    /**
     *
     * @return Hash code of the entity
     */
    @Override
    public int hashCode() {
        return (id == null) ? -1 : id.hashCode();
    }
}
