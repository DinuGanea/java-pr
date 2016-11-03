
import factory.PageFactory;
import model.Page;
import output.PageExport;
import util.Loggable;

import java.util.HashSet;
import java.util.Set;


public class Main implements Loggable {

    public static void main(String args[]) {


        String filepath = Main.class.getResource("wikipedia_de_prgpr_subset.txt").getPath();

        try {
            Set<Page> pages = PageFactory.build(filepath);
            PageExport ep = new PageExport();
            ep.exportToXML(pages);
        } catch (Exception e) {
            logger.info("An error occured!");
            e.printStackTrace();
        }

    }
}
