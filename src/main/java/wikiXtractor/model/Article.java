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

    @Relationship(type = Reference.TYPE, direction = Relationship.OUTGOING)
    private Set<Article> referals;

    public Article() {
        categories = new HashSet<>();
        referals = new HashSet<>();
    }

    public Article(String pageTitle) {
        this("", pageTitle);
    }


    public Article(String pageID, String pageTitle) {
        this.namespaceID = Article.NAMESPACE_ID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
        this.customID = stringHashCode();

        categories = new HashSet<>();
        referals = new HashSet<>();
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

    public Article addReferal(Article referal) {
        referals.add(referal);
        return this;
    }

    public Set<Article> getReferals() {
        return referals;
    }
}
