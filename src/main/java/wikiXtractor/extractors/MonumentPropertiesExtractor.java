package wikiXtractor.extractors;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import wikiXtractor.mapper.MonumentMapper;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Identify and extract possible properties for a monument
 *
 * Monument pages don't contain metadata as the person pages do, so we'll have to go the tricky way.
 * We know already that the inauguration year can appear on the page in a exact sequence. Same for the
 * site property. The name is extracted thanks to the article name. Other properties are in need of entity labeling
 *
 *
 * @author Dinu Ganea
 */
public class MonumentPropertiesExtractor {

    /**
     * Find and extract properties that define a monument
     *
     * @param content String that is supposed to contain need information
     * @return HashMap of properties
     */
    public static HashMap<String, String> extract(String content) {
        HashMap<String, String> properties = new HashMap<>();

        // Define pattern that should contain the inauguration year
        Pattern p = Pattern.compile("Erbaut in den ([0-9]{4})er");
        Matcher m = p.matcher(content);

        if (m.find()) {
            // If we found a match, we can be sure that the location is wrapped in the 1st group
            properties.put(MonumentMapper.INAUGURATION_YEAR, m.group(1));
        }

        // Search for the location
        p = Pattern.compile("(?:.*denkmal i[nm]) (.*?)\\s", Pattern.CASE_INSENSITIVE);
        m = p.matcher(content);

        if (m.find()) {
            // If we found a match, we can be sure that the location is wrapped in the 1st group
            properties.put(MonumentMapper.SITE, m.group(1));
        }


        return properties;
    }

}
