package wikiXtractor.model;

import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Objects;
import java.util.Set;

/**
 * Page Object class. This is a totally reusable independent component.
 *
 * @author Delara Nasri
 */
@NodeEntity
public class Page extends Entity {

    /**
     * Needed for XML definition later on (PageExport class)
     */
    public static final String NS_EL_NAME = "namespaceID";
    public static final String PAGE_ID_EL_NAME = "pageID";
    public static final String PAGE_TITLE_EL_NAME = "title";
    public static final String CAT_ROOT_EL_NAME = "categories";
    public static final String CAT_EL_NAME = "category";
    public static final String CAT_ATT_NAME = "name";

    private int namespaceID;
    private String pageID;
    private String pageTitle;
    private String htmlContent;

    @Relationship(type = "HAS_CATEGORY")
    private Set<Category> categories;


    public Page() {

    }


    /**
     * Page constructor.
     *
     * @param namespaceID page namespace id
     * @param pageID      page id
     * @param pageTitle   page title
     */
    public Page(int namespaceID, String pageID, String pageTitle) {
        this.namespaceID = namespaceID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
    }

    /**
     * @return page namespace id
     */
    public int getNamespaceID() {
        return namespaceID;
    }

    /**
     * @param namespaceID page namespace id
     */
    public void setNamespaceID(int namespaceID) {
        this.namespaceID = namespaceID;
    }

    /**
     * @return page id
     */
    public String getPageID() {
        return pageID;
    }

    /**
     * @param pageID page id
     */
    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    /**
     * @return title of page
     */
    public String getPageTitle() {
        return pageTitle;
    }

    /**
     * @param pageTitle -title of page
     */
    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
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


    /**
     * @return html content of the page
     */
    public String getHtmlContent() {
        return htmlContent;
    }

    /**
     * @param htmlContent - html content of one single article
     */
    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    /**
     * Specify display method of the object
     *
     * @return string conversion of the object
     */
    public String toString() {
        return String.format("%s %s %s", namespaceID, pageID, pageTitle);
    }

    /**
     * Compare this to another object
     *
     * @param obj potential page object
     * @return true if namespaceID and title are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Page)) {
            return false;
        } else {
            if (this.namespaceID == ((Page) obj).getNamespaceID() && this.pageTitle.equals(((Page) obj).getPageTitle())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create hash code for a page instance
     *
     * @return hash code out of namespaceID and pageTitle
     */
    @Override
    public int hashCode() {
        return Objects.hash(namespaceID, pageTitle);
    }

}
