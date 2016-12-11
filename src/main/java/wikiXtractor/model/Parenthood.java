/*
package wikiXtractor.model;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

*/
/**
 * Entity for parenthood relationship
 *
 * @author Dinu Ganea
 *//*

@RelationshipEntity(type = Parenthood.TYPE)
public class Parenthood extends Entity {

    // Relationship label
    public static final String TYPE = "PARENT_OF";

    @StartNode
    private Page parent;

    @EndNode
    private Category child;


    */
/**
     * Set the parent object
     *
     * @param parent Parent category
     * @return Parenthood object
     *//*

    public Parenthood setParent(Page parent) {
        this.parent = parent;
        return this;
    }

    */
/**
     * Set the child category object
     *
     * @param child Child category
     * @return Parenthood object
     *//*

    public Parenthood setChild(Category child) {
        this.child = child;
        return this;
    }

    */
/**
     * Get parent category
     *
     * @return Parent category
     *//*

    public Page getParent() {
        return parent;
    }

    */
/**
     * Get child category
     *
     * @return Child category
     *//*

    public Page getEndNode() {
        return child;
    }


}
*/


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
