package wikiXtractor.api.cli;

import com.beust.jcommander.Parameters;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.transaction.Transaction;
import wikiXtractor.Main;
import wikiXtractor.extractors.LinkExtraction;
import wikiXtractor.extractors.PersonPropertiesExtractor;
import wikiXtractor.factory.PageFactory;
import wikiXtractor.mapper.PersonMapper;
import wikiXtractor.model.*;
import wikiXtractor.neo4j.manager.SessionManager;
import wikiXtractor.service.*;
import wikiXtractor.util.DirectoryManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * For each person node in the database, extract valuable information
 * <p>
 * PersonExtraction <<db-directory>>
 *
 * @author Sonia Rooshenas
 */
@Parameters(commandDescription = "Extract information about person")
public class PersonExtractionCommand extends CLICommand<Void> {

    // command name
    private static final String NAME = "PersonExtraction";

    // absolute/relative path to db directory
    private String dbDirectoryURI;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CLICommand extractParameters() throws Exception {

        dbDirectoryURI = DirectoryManager.createFullPathTo(input.get(0));

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Void execute() throws Exception {

        logger.info("Executing {} command!", getName());

        // Default routine
        validateInput(1);
        extractParameters();

        // Init session
        SessionManager sessionManager = SessionManager.getInstance(dbDirectoryURI, Main.DOMAIN_NAME);
        Session session = sessionManager.getSession();

        // Get an instance of the person mapper
        PersonMapper mapper = PersonMapper.getInstance();

        session.beginTransaction();

        try {

            PersonService ps = new PersonService(session);
            MonumentService ms = new MonumentService(session);
            CityService cs = new CityService(session);

            HashMap<String, Person> persons = ps.findAllContentEntities();
            HashMap<String, Monument> monuments = ms.findAllContentEntities();
            HashMap<String, City> cities = cs.findAllContentEntities();


            logger.info(String.format("%d content entities of type 'Person' loaded", persons.size()));

            int queriesToExecute = 0;
            int references = 0;
            for (Person p : persons.values()) {

                // Extract information about the person
                HashMap<String, String> properties = PersonPropertiesExtractor.extract(p.getSourcePage().getHtmlContent());

                // Assign each property to it's attribute
                mapper.map(p, properties);

                // Extract all hyperlinks
                HashMap<String, String> links = LinkExtraction.extractLinks(p.getSourcePage().getHtmlContent(), PageFactory.LINKS_SELECTOR, p.getSourcePage().getPageTitle());

                // If the link matches the raw title of the object in the list, we'll create a reference
                for (String link : links.keySet()) {
                    references++;
                    if (monuments.containsKey(link)) {
                        p.addReferral(monuments.get(link), ContentEntity.MONUMENT_NODE);
                    } else if (persons.containsKey(link)) {
                        p.addReferral(persons.get(link), ContentEntity.PERSON_NODE);
                    } else if (cities.containsKey(link)) {
                        p.addReferral(cities.get(link), ContentEntity.CITY_NODE);
                    } else {
                        references = references > 0 ? references - 1 : 0;
                    }
                }

                // Update the object
                ps.upsert(p);

                queriesToExecute++;

                if (queriesToExecute % 2000 == 0) {
                    logger.info("{} person objects updated to database with a total of {} references", queriesToExecute, references);
                    session.getTransaction().commit();
                    session.beginTransaction();
                }
            }

            session.getTransaction().commit();

        } catch (Exception e) {
            if (session.getTransaction().status() != Transaction.Status.OPEN) {
                session.getTransaction().rollback();
            }
            throw e;
        }

        logger.info("{} command successfully executed!", getName());
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Set<String> getDependencies() {
        return new HashSet<String>() {{
            add(ImportHTMLCommand.class.getSimpleName());
            add(EntityExtractionCommand.class.getSimpleName());
        }};
    }
}
