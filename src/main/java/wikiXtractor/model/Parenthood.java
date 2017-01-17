package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Entity for categorisation relationship
 *
 * @author Delara Nasri
 */
@RelationshipEntity(type = Parenthood.TYPE)
public class Parenthood extends Entity {

    // Relationship label
    public static final String TYPE = "PARENT_OF";

    @StartNode
    private Page page;

    @EndNode
    private Category category;

    /**
     * Set page objects
     *
     * @param p Page object
     * @return Categorisation object
     */
    public Parenthood setPage(Page p) {
        this.page = p;
        return this;
    }

    /**
     * Set category object
     *
     * @param c Category object
     * @return Categorisation object
     */
    public Parenthood setCategory(Category c) {
        this.category = c;
        return this;
    }

    /**
     * Return categorised page object
     *
     * @return Page object
     */
    public Page getPage() {
        return page;
    }

    /**
     * Returns category of the object
     *
     * @return Category object
     */
    public Category getCategory() {
        return category;
    }


}
