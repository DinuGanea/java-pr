package model;

import java.io.Serializable;
import java.util.Set;

/**
 * This is a totally reusable component
 */
public class Page {

    /**
     * Needed for XML definition later on (PageExport class)
     */
    public static final String NS_EL_NAME = "namespaceID";
    public static final String ART_ID_EL_NAME = "articleID";
    public static final String ART_TITLE_EL_NAME = "articleTitle";
    public static final String CAT_ROOT_EL_NAME = "categories";
    public static final String CAT_EL_NAME = "category";
    public static final String CAT_ATT_NAME = "name";

    private String namespaceID;
    private String articleID;
    private String articleTitle;
    private Set<String> categories;

    /**
     * Default empty constructor. Used to create a model instance of page
     */
    public Page() {

    }

    /**
     * @param namespaceID - page namespace id
     * @param articleID - page id
     * @param articleTitle - page title
     */
    public Page(String namespaceID, String articleID, String articleTitle) {
        this.namespaceID = namespaceID;
        this.articleID = articleID;
        this.articleTitle = articleTitle;
    }

    /**
     * @param namespaceID - page namespace id
     * @param articleID - page id
     * @param articleTitle - page title
     * @param categories - set of categories
     *
     */
    public Page(String namespaceID, String articleID, String articleTitle, Set<String> categories) {
        this(namespaceID, articleID, articleTitle);
        this.categories = categories;
    }

    /**
     * @return - page namespace id
     */
    public String getNamespaceID() {
        return namespaceID;
    }

    /**
     * @param namespaceID - page namespace id
     */
    public void setNamespaceID(String namespaceID) {
        this.namespaceID = namespaceID;
    }

    /**
     * @return - page id
     */
    public String getArticleID() {
        return articleID;
    }

    /**
     * @param articleID - page id
     */
    public void setArticleID(String articleID) {
        this.articleID = articleID;
    }

    /**
     * @return - title of page
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     * @param articleTitle -title of page
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    /**
     * @return - set of page categories
     */
    public Set<String> getCategories() {
        return categories;
    }

    /**
     * @param categories - categories' set
     */
    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    /**
     * Specify display method of the object
     *
     * @return string conversion of the object
     */
    public String toString() {
        return String.format("%s %s %s", namespaceID, articleID, articleTitle);
    }

}
