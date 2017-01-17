package wikiXtractor.extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wikiXtractor.util.Loggable;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Used initially only to extract categories out of a html page
 *
 * @author Ionut Urs
 */
public class LinkExtraction implements Loggable {

    /**
     * Retrieve specific links from html content
     *
     * @param rawContent an entire article html page
     * @param selector css selector for the category elements
     * @param objectID id of the object. Used only for logging reference. set it to "" if no reference is available
     * @return set of links' titles (the order won't be guaranteed, but it's not a problem)
     */
    public static HashMap<String, String> extractLinks(String rawContent, String selector, String objectID) {
        HashMap<String, String> links = new HashMap<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements elements = doc.select(selector);

        for (Element link : elements) {
            // Parse through each element an get it's title attribute (the actual category)
            // please make sure that the attribute name match the input format
            links.put(link.attr("title"), link.attr("href"));
        }

        // Warn the user that this page doesn't seem to have any category declared
        if (links.isEmpty() || objectID.length() == 0) {
            logger.warn(String.format("No links found for the object with id %s!", objectID));
        }

        return links;
    }

    /**
     * Retrieve specific links that match a given pattern from html content
     *
     * @param rawContent an entire article html page
     * @param selector css selector for the category elements
     * @param hrefPattern regex pattern that every href should match in order it's title to be added to the set
     * @param objectID id of the object. Used only for logging reference. set it to "" if no reference is available
     * @return set of links' titles (the order won't be guaranteed, but it's not a problem)
     */
    public static HashMap<String, String> extractLinks(String rawContent, String selector, String hrefPattern, String objectID) {
        HashMap<String, String> links = new HashMap<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements elements = doc.select(selector);

        for (Element link : elements) {

            if (link.attr("href").matches(hrefPattern)) {
                // Parse through each element an get it's title attribute (the actual category)
                // please make sure that the attribute name match the input format
                links.put(link.attr("title"), link.attr("href"));
            }

        }

        // Warn the user that this page doesn't seem to have any category declared
        if (links.isEmpty() || objectID.length() == 0) {
            logger.warn(String.format("No links found for the object with id %s!", objectID));
        }

        return links;
    }
}
