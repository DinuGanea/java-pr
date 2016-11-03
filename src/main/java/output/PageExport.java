package output;


import model.Page;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import util.Loggable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Set;

public class PageExport implements Loggable {

    protected static final String ROOT_NAME = "pages";
    protected static final String RECORD_NAME = "page";


    /**
     * Write a set of page objects to a xml file
     *
     * @param pageSet - set of page objects
     * @throws Exception
     */
    public void exportToXML(Set<Page> pageSet) throws Exception {

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
                attr.setValue(p.getNamespaceID());
                pageElement.setAttributeNode(attr);

                // articleID attribute
                attr = doc.createAttribute(Page.ART_ID_EL_NAME);
                attr.setValue(p.getArticleID());;
                pageElement.setAttributeNode(attr);

                // articleTitle attribute
                attr = doc.createAttribute(Page.ART_TITLE_EL_NAME);
                attr.setValue(p.getArticleTitle());
                pageElement.setAttributeNode(attr);

                // create categories parent element
                Element categories = doc.createElement(Page.CAT_ROOT_EL_NAME);

                // Parse each category and create an element for it
                for (String catName : p.getCategories()) {
                    // append each category to its parent
                    categories.appendChild(createCategory(catName, doc));
                }

                // add categories to the page
                pageElement.appendChild(categories);

                // add new page to document
                root.appendChild(pageElement);

            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(doc);

            // initiating a stream to a specific file
            StreamResult result = new StreamResult(new File("pages.xml"));

            // writing stream-wise to output file
            transformer.transform(source, result);


        } catch (Exception e) {
            // this actually treats 3 Exceptions, but it's no need to handle them all separately
            logger.error("Couldn't write page objects to XML file");
            throw e;
        }

    }

    /**
     * Create a category element by provided name and DOM element
     *
     * @param catName - category name
     * @param doc - dom document
     * @return - category xml element
     */
    public Element createCategory(String catName, Document doc) {
        Element category = doc.createElement(Page.CAT_EL_NAME);
        Attr attr = doc.createAttribute(Page.CAT_ATT_NAME);
        attr.setValue(catName);
        category.setAttributeNode(attr);
        return category;
    }


}
