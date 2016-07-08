package rest;

import org.glassfish.jersey.server.ResourceConfig;
import org.kududb.client.KuduClient;

import java.io.IOException;

/**
 * Created by andrew.wong on 7/7/16.
 */
public class RESTServlet {

    private final KuduClient kclient;
    private final ResourceConfig conf;
    private static RESTServlet INSTANCE;

    public KuduClient getClient() {
        return kclient;
        }

    public synchronized static RESTServlet getInstance(ResourceConfig conf,
                                                           KuduClient client) throws IOException {
        if (INSTANCE == null) {
            INSTANCE = new RESTServlet(conf, client);
            }
        return INSTANCE;
        }

    public synchronized static RESTServlet getInstance() {
        assert(INSTANCE != null);
        return INSTANCE;
        }

    RESTServlet(final ResourceConfig conf,
                    final KuduClient client) throws IOException {
        this.kclient = client;
        this.conf = conf;

//registerCustomFilter(conf);
//int cleanInterval = conf.getInt(CLEANUP_INTERVAL, 10 * 1000);
//int maxIdleTime = conf.getInt(MAX_IDLETIME, 10 * 60 * 1000);
//connectionCache = new ConnectionCache(
//conf, userProvider, cleanInterval, maxIdleTime);
//if (supportsProxyuser()) {
//ProxyUsers.refreshSuperUserGroupsConfiguration(conf);
//}
//
//metrics = new MetricsREST();
//pauseMonitor = new JvmPauseMonitor(conf, metrics.getSource());
//pauseMonitor.start();
        }
}