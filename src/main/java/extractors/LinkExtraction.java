package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Loggable;

import java.util.HashSet;

/**
 * Used initially only to extract categories out of a html page
 *
 * @author Ionut Urs
 */
public class LinkExtraction implements Loggable {

    // Path to the categories in html article (css selector)
    protected static final String CATS_SELECTOR = "div#catlinks ul li a";

    /**
     * Retrieve categories from html content
     *
     * @param rawContent - an entire article html page
     * @param pageID - id of the page object. Used only for logging reference
     * @return - set of categories (the order won't be guaranteed, but it's not a problem)
     */
    public static HashSet<String> getCategories(String rawContent, String pageID) {
        HashSet<String> categories = new HashSet<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements links = doc.select(CATS_SELECTOR);

        for (Element link : links) {
            // Parse through each element an get it's title attribute (the actual category)
            // please make sure that the attribute name match the input format
            categories.add(link.attr("title"));
        }

        // Warn the user that this page doesn't seem to have any category declared
        if (categories.isEmpty()) {
            logger.warn(String.format("No categories for the page %s!", pageID));
        }

        return categories;
    }
}
