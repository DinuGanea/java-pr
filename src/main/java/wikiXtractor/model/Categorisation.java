package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Entity for categorisation relationship
 *
 * @author Delara Nasri
 */
@RelationshipEntity(type = Categorisation.TYPE)
public class Categorisation extends Entity {

    // Relationship label
    public static final String TYPE = "CATEGORY_OF";

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
    public Categorisation setPage(Page p) {
        this.page = p;
        return this;
    }

    /**
     * Set category object
     *
     * @param c Category object
     * @return Categorisation object
     */
    public Categorisation setCategory(Category c) {
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
