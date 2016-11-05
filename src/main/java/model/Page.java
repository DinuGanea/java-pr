package model;

import java.util.Set;

/**
 * Page Object class. This is a totally reusable independent component.
 *
 * @author Delara Nasri
 */
public class Page {

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
    private Set<String> categories;

    /**
     * Page constructor.
     *
     * @param namespaceID - page namespace id
     * @param pageID - page id
     * @param pageTitle - page title
     */
    public Page(int namespaceID, String pageID, String pageTitle) {
        this.namespaceID = namespaceID;
        this.pageID = pageID;
        this.pageTitle = pageTitle;
    }

    /**
     * @return - page namespace id
     */
    public int getNamespaceID() {
        return namespaceID;
    }

    /**
     * @param namespaceID - page namespace id
     */
    public void setNamespaceID(int namespaceID) {
        this.namespaceID = namespaceID;
    }

    /**
     * @return - page id
     */
    public String getPageID() {
        return pageID;
    }

    /**
     * @param pageID - page id
     */
    public void setPageID(String pageID) {
        this.pageID = pageID;
    }

    /**
     * @return - title of page
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
        return String.format("%s %s %s", namespaceID, pageID, pageTitle);
    }

}
