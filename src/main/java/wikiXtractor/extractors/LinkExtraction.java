package wikiXtractor.extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wikiXtractor.util.Loggable;

import java.util.HashSet;

/**
 * Used initially only to extract categories out of a html page
 *
 * @author Ionut Urs
 */
public class LinkExtraction implements Loggable {

    /**
     * Retrieve categories from html content
     *
     * @param rawContent an entire article html page
     * @param selector css selector for the category elements
     * @param objectID id of the object. Used only for logging reference. set it to "" if no reference is available
     * @return set of categories (the order won't be guaranteed, but it's not a problem)
     */
    public static HashSet<String> extractCategories(String rawContent, String selector, String objectID) {
        HashSet<String> categories = new HashSet<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements links = doc.select(selector);

        for (Element link : links) {
            // Parse through each element an get it's title attribute (the actual category)
            // please make sure that the attribute name match the input format
            categories.add(link.attr("title"));
        }

        // Warn the user that this page doesn't seem to have any category declared
        if (categories.isEmpty() || objectID.length() == 0) {
            logger.warn(String.format("No categories found for the object with id %s!", objectID));
        }

        return categories;
    }
}
