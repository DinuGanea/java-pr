package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Article extends Page {

    public static final int NAMESPACE_ID = 0;

    @Relationship(type = Categorisation.TYPE, direction = Relationship.INCOMING)
    private Set<Category> categories;


    public Article() {

    }

    public Article(int namespaceID, String pageTitle) {
        this(namespaceID, "", pageTitle);
    }


    public Article(int namespaceID, String pageID, String pageTitle) {
        this.namespaceID = namespaceID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
        this.customID = hashCode();

        categories = new HashSet<>();
    }


    /**
     * @return set of page categories
     */
    public Set<Category> getCategories() {
        return categories;
    }

    /**
     * @param categories categories' set
     */
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Article addCategory(Category category) {
        categories.add(category);
        return this;
    }
}
