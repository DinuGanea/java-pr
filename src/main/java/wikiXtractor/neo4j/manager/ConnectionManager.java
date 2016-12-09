/*
package wikiXtractor.neo4j.manager;

import org.neo4j.cypher.internal.ExecutionEngine;
import org.neo4j.cypher.internal.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.parboiled.common.StringUtils;
import wikiXtractor.model.Entity;
import wikiXtractor.model.Page;
import wikiXtractor.model.Parenthood;
import wikiXtractor.util.DirectoryManager;
import wikiXtractor.util.Loggable;

import java.io.File;
import java.util.*;

*/
/**
 * Created by dinu on 12/9/16.
 *//*

public class ConnectionManager implements Loggable {

    public GraphDatabaseService graphDB;

    public ConnectionManager(String dbUri) {

        graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(new File(DirectoryManager.getFullPath(dbUri)));
        registerShutdownHook(graphDB);

    }

    public void insert(Set<Page> objects) {


        try (Transaction tx = graphDB.beginTx()) {
            // Database operations go here

            int i = 0;
            for (Page page : objects) {

                i++;
                String query = String.format("CREATE (:%s:Page{namespaceID:'%s', pageTitle:'%s', pageID:'%s', htmlContent:'%s', customID:'%s'})",
                        page.getClass().getSimpleName(),
                        page.getNamespaceID(),
                        page.getCatTitles(),
                        page.getPageID(),
                        "", //StringUtils.escape(page.getHtmlContent()),
                        page.getCustomID()
                );


                //logger.debug(query);

                */
/**
                 * CREATE (adam:User { name: 'Adam' }),(pernilla:User { name: 'Pernilla' }),(david:User { name: 'David'
                 }),
                 (adam)-[:FRIEND]->(pernilla),(pernilla)-[:FRIEND]->(david)
                 *//*



                graphDB.execute(query);

                if (i % 2000 == 0) {

                    logger.info(String.format("%d pages inserted", i));
                    tx.success();
                    tx.close();
                    graphDB.beginTx();
                }


            }

            tx.success();
        }

    }

    int i = 0;
    public void createRelationship(String parent, String child) {

        i++;
        Transaction tx = graphDB.beginTx();
        try  {
            String query = String.format("MATCH (p:Category{%s:'%s'}), (c:Category{%s:'%s'}) " +
                            "CREATE (p)-[:%s]->(c)"
                    , Entity.CUSTOM_ID_PROP_NAME, parent
                    , Entity.CUSTOM_ID_PROP_NAME, child
                    , Parenthood.TYPE
            );

            graphDB.execute(query);

            if (i % 2000 == 0) {

                logger.info(String.format("%d relationships inserted", i));
                tx.success();
                tx.close();
                tx = graphDB.beginTx();
            }

            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Result findAll() {
        Transaction tx = graphDB.beginTx();

        ExecutionEngine execEngine = new ExecutionEngine(graphDB);
        ExecutionResult execResult = execEngine.execute("MATCH (java:JAVA) RETURN java");
        String results = execResult.dumpToString();
        System.out.println(results);


        Map<String,Object> params=new HashMap<String,Object>();
        params.put("city_name","dhaka");
        Result result = graphDB.execute("MATCH (city:City {city:{city_name})<-[:LIVES_IN]-(person) RETURN person",params);



        //IndexHits<Node> hits =  // categories.get( "namespaceID", "14");


        tx.success();
        tx.close();

        return result;

    }



    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }
}
*/
