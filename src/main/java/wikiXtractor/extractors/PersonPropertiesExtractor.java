package wikiXtractor.extractors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import wikiXtractor.util.Loggable;

import java.util.HashMap;

/**
 * Identify and extract possible properties for a person
 *
 * All html documents contain a metadata table that summarise information about a person, so we'll access only it.
 * It's not wise to make other assumptions, that exact matches
 *
 * @author Dinu Ganea
 */
public class PersonPropertiesExtractor implements Loggable {

    /**
     * Find and extract properties that define a person
     *
     * @param rawContent Content that is supposed to contain need information
     * @return HashMap of properties
     */
    public static HashMap<String, String> extract(String rawContent) {
        HashMap<String, String> properties = new HashMap<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements elements = doc.select("#Vorlage_Personendaten tr");

        // Parse each property and add it to the set
        elements.forEach(e -> {
            Elements columns = e.select("td");

            if (columns.size() == 2) {
                String key = columns.first().text();
                String property = columns.last().text();

                properties.put(key, property);
            }
        });


        return properties;
    }


}
