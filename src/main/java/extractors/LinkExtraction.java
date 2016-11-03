package extractors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Loggable;
import java.util.HashSet;


public class LinkExtraction implements Loggable {
    /**
     * fdfgdf
     *
     * @param rawContent - ji
     * @return -
     */
    public HashSet<String> getCategories(String rawContent){
        HashSet<String> categories = new HashSet<String>();
        Document doc = Jsoup.parse(rawContent);
        Elements links = doc.getElementsByTag("a");
        for (Element link : links) {
            categories.add(link.attr("title"));
        }

        return categories;


    }
}
