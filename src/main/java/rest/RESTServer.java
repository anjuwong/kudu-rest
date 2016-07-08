package rest;
/**
 * Created by andrew.wong on 7/7/16.
 */

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.kududb.ColumnSchema;
import org.kududb.Schema;
import org.kududb.Type;
import org.kududb.client.*;

import java.util.ArrayList;
import java.util.List;

public class RESTServer {
    private static final String KUDU_MASTER = System.getProperty(
            "kuduMaster", "quickstart.cloudera");

    private KuduTable table;
    private KuduTable idTable;

    public static void main(String[] args) throws Exception {
        KuduClient client = new KuduClient.KuduClientBuilder("172.26.10.68").build();
        ResourceConfig config = new ResourceConfig();
        config.packages("rest");

        RESTServlet.getInstance(config, client);

        // Jersey (REST) servlet
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));

        // Change the port number that we're running on
        Server server = new Server(8787);

        // Holds the Jersey (REST) servlet
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");

        try {
            server.start();
            server.join();
            } finally {
            server.destroy();
        }
    }
}
