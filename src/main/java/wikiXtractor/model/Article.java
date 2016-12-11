package wikiXtractor.model;


import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.HashSet;
import java.util.Set;

/**
 * Entity for the Article node
 *
 * @author Delara Nasri
 */
@NodeEntity
public class Article extends Page {

    // Defined namespace for this type of node
    public static final int NAMESPACE_ID = 0;

    // Categories of the article
    @Relationship(type = Categorisation.TYPE, direction = Relationship.INCOMING)
    private Set<Category> categories = new HashSet<>();

    // Articles, this object refers to
    @Relationship(type = Reference.TYPE, direction = Relationship.OUTGOING)
    private Set<Article> referrals = new HashSet<>();

    // Articles, that refer to this object
    @Relationship(type = Reference.TYPE, direction = Relationship.INCOMING)
    private Set<Article> referrers = new HashSet<>();

    /**
     * The OGM requires an public no-args constructor to be able to construct objects.
     */
    public Article() {
    }


    /**
     * Create a new article object from the given name
     *
     * @param pageTitle Article name
     */
    public Article(String pageTitle) {
        this("", pageTitle);
    }

    /**
     * Create a new article object from given id und title
     *
     * @param pageID Article identifier
     * @param pageTitle Article title
     */
    public Article(String pageID, String pageTitle) {
        this.namespaceID = Article.NAMESPACE_ID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
        this.customID = stringHashCode();
    }


    /**
     * Get article categories
     *
     * @return set of page categories
     */
    public Set<Category> getCategories() {
        return categories;
    }

    /**
     * Set article categories
     *
     * @param categories categories' set
     */
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }


    /**
     * Add a single category to the article
     *
     * @param category Category object
     * @return this
     */
    public Article addCategory(Category category) {
        categories.add(category);
        return this;
    }

    /**
     * Add a single referral to the article
     *
     * @param referal Article object
     * @return this
     */
    public Article addReferral(Article referal) {
        referrals.add(referal);
        return this;
    }

    /**
     * Get list the list of referrals
     *
     * @return List of articles
     */
    public Set<Article> getReferrals() {
        return referrals;
    }

    /**
     * Get list of referrers
     *
     * @return List of articles
     */
    public Set<Article> getReferrers() {return referrers; }
}
