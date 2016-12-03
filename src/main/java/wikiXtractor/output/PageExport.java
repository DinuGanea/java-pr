package wikiXtractor.output;


import wikiXtractor.model.Category;
import wikiXtractor.model.Page;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import wikiXtractor.util.Loggable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Set;

/**
 * Used to write export objects (only file export is implemented right now)
 *
 * @author Sonia Rooshenas
 */
public class PageExport implements Loggable {

    // XML element names
    protected static final String ROOT_NAME = "pages";
    protected static final String RECORD_NAME = "page";

    private String outFileName;

    public PageExport(String outFileName) {
        this.outFileName = outFileName;
    }


    /**
     * Export a set of page objects to a xml file
     *
     * @param pageSet set of page objects
     * @throws Exception
     */
    public void exportToXML(Set<Page> pageSet) throws Exception {

        // Needed for logging
        int pagesNr = pageSet.size();

        logger.info(String.format("Received %d page objects to export", pagesNr));
        logger.info("Parsing pages and start exporting");

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();

            // page container
            Element root = doc.createElement(ROOT_NAME);
            doc.appendChild(root);

            // Adding pages to XML
            for (Page p : pageSet) {

                Element pageElement = doc.createElement(RECORD_NAME);

                // namespaceID attribute
                Attr attr = doc.createAttribute(Page.NS_EL_NAME);
                attr.setValue(p.getNamespaceID() + "");
                pageElement.setAttributeNode(attr);

                // pageID attribute
                attr = doc.createAttribute(Page.PAGE_ID_EL_NAME);
                attr.setValue(p.getPageID());;
                pageElement.setAttributeNode(attr);

                // pageTitle attribute
                attr = doc.createAttribute(Page.PAGE_TITLE_EL_NAME);
                attr.setValue(p.getPageTitle());
                pageElement.setAttributeNode(attr);

                // create categories' parent element
                Element categories = doc.createElement(Page.CAT_ROOT_EL_NAME);

                // Parse each category and create an element for it
                for (Category cat : p.getCategories()) {
                    // the category element and it's children could be changed easily from the method
                    // append each category to its parent
                    categories.appendChild(createCategory(cat.getName(), doc));
                }

                // add categories to the page
                pageElement.appendChild(categories);

                // add new page to document
                root.appendChild(pageElement);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Make sure the output file will be appropriate formatted. It's silly to have whole XML into a single line
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Set the amount of ident (go with 2 by default)
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Create an input source form the DOM document
            DOMSource source = new DOMSource(doc);

            logger.info(String.format("Creating the output file \"%s\"", this.outFileName));

            // Creating the output file
            File output = new File(this.outFileName);
            // Make sure to construct the path to the file
            if (output.getParentFile() != null && !output.getParentFile().exists() && !output.getParentFile().mkdirs()) {
                throw new Exception("Cannot create the path to the output file!");
            }

            // initiating a stream to a specific file
            StreamResult result = new StreamResult(output);

            logger.info("Saving xml content to file");

            // writing stream-wise to output file
            transformer.transform(source, result);

            logger.info("Output file successfully populated!");

        } catch (Exception e) {
            // this actually treats 3 Exceptions, but it's no need to handle them all separately
            logger.error("Couldn't write page objects to XML file. Check the log for more details");
            throw e;
        }

    }

    /**
     * Create a category element by provided name and DOM element
     *
     * @param catName category name
     * @param doc dom document
     * @return category xml element
     */
    public Element createCategory(String catName, Document doc) {
        Element category = doc.createElement(Page.CAT_EL_NAME);
        Attr attr = doc.createAttribute(Page.CAT_ATT_NAME);
        attr.setValue(catName);
        category.setAttributeNode(attr);
        return category;
    }


}
