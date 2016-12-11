package wikiXtractor.model;


import javafx.scene.Parent;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity for category node
 *
 * @author Delara Nasri
 */
@NodeEntity
public class Category extends Page {

    // Defined namespace for this entity type
    public static final int NAMESPACE_ID = 14;

    /**
     * Set of subcategories of this category
     */
    @Relationship(type = Parenthood.TYPE, direction = Relationship.OUTGOING)
    private Set<Category> subcategories = new HashSet<>();


    /**
     * Set of parent categories of this category
     */
    @Relationship(type = Parenthood.TYPE, direction = Relationship.INCOMING)
    private Set<Category> supercategories = new HashSet<>();

    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public Category() {
    }

    /**
     * Create an new Category from given properties
     * We don't know namespaceID, cause we already know it
     *
     * @param pageID ID of page object
     * @param pageTitle Title of page object
     */
    public Category(String pageID, String pageTitle) {
        this.namespaceID = NAMESPACE_ID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
        this.customID = stringHashCode();
    }

    /**
     * Return list of all subcategories of this category
     *
     * @return List of subcategories
     */
    public Set<Category> getSubcategories() {
        return subcategories;
    }

    /**
     * Set child categories of this category
     *
     * @param subcategories List of child categories
     * @return Category object
     */
    public Category setSubcategories(Set<Category> subcategories) {
        this.subcategories = subcategories;
        return this;
    }

    /**
     * Add one single subcategory to the set
     *
     * @param subcategory Child category
     * @return Category object
     */
    public Category addSubcategory(Category subcategory) {

        subcategories.add(subcategory);
        return this;
    }

    public Set<Category> getSupercategories() {
        return supercategories;
    }
}
