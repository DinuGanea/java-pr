package wikiXtractor.extractors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import wikiXtractor.mapper.CityMapper;
import wikiXtractor.util.Loggable;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Identify and extract possible properties for a city
 *
 * City pages contain a metadata table, just like person pages, which is the source of our properties. All we can found, we'll push into a HashSet
 * and define as attributes of a city object
 *
 * @author Dinu Ganea
 */
public class CityPropertiesExtractor implements Loggable {

    /**
     * Find and extract properties that define a city
     *
     * @param rawContent String that is supposed to contain need information
     * @return HashMap of properties
     */
    public static HashMap<String, String> extract(String rawContent) {
        HashMap<String, String> properties = new HashMap<>();

        // Convert raw html code to DOM document
        Document doc = Jsoup.parse(rawContent);

        // Apply the selector on the entire DOM document
        Elements elements = doc.select("table.hintergrundfarbe5.float-right tr");

        // A row element of the table contains both (property name and property value)
        elements.forEach(e -> {
            Elements columns = e.select("td");
            if (columns.size() == 2) {
                if (!columns.first().attr("style").equals("width: 50%;")) {
                    properties.put(columns.first().text(), columns.last().text());
                }
            }


        });

        /**
         * Foundation year is a little bit tricky, some pages contain the following pattern that allows to identify it quite easy,
         * so we'll use this shortcut
         */
        Pattern p = Pattern.compile("Ersterwähnung ([0-9]{4})");
        Matcher m = p.matcher(rawContent);

        if (m.find()) {
            // Group 1 should contain the info about the year/date
            properties.put(CityMapper.FOUNDATION_YEAR_KEY, m.group(1));
        }


        return properties;
    }


}
