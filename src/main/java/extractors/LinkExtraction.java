package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import util.Loggable;

import java.util.HashSet;

/**
 * Used initially only to extract categories out of a html page
 */
public class LinkExtraction implements Loggable {

    // Path to the categories in html article
    protected static final String CATS_SELECTOR = "div#catlinks ul li a";

    /**
     * Retrieve categories from html content
     *
     * @param rawContent - an entire article html page
     * @return - set of categories (the order won't be guaranteed, but it's not a problem though)
     */
    public static HashSet<String> getCategories(String rawContent) {
        HashSet<String> categories = new HashSet<String>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements links = doc.select(CATS_SELECTOR);

        for (Element link : links) {
            // Parse through each element an get it's title attribute (the actual category)
            categories.add(link.attr("title"));
        }

        return categories;
    }
}
