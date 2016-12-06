package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Set;

@NodeEntity
public class Category extends Page {

    public static final int NAMESPACE_ID = 14;

    public Category() {

    }

    public Category(int namespaceID, String pageTitle) {
        this(namespaceID, "", pageTitle);
    }


    public Category(int namespaceID, String pageID, String pageTitle) {
        this.namespaceID = namespaceID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
        this.customID = hashCode();
    }

    @Relationship(type = "PARENT_OF")
    private Set<Category> subcategories;


    public Set<Category> getSubcategories() {
        return subcategories;
    }

    public Category setSubcategories(Set<Category> subcategories) {
        this.subcategories = subcategories;
        return this;
    }
}
