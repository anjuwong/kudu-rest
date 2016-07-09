package rest;

import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;
import org.json.JSONArray;
import org.json.JSONObject;
import org.kududb.client.*;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.ArrayList;


@Path("scan")
public class ScanDataResource {
    @GET
    @Produces("application/json")
    public String scan(String s) throws Exception {
        JSONObject obj = new JSONObject(s);
        JSONArray columns = obj.getJSONArray("Columns");

        final ArrayList<String> projectColumns = new ArrayList<String>(1);
        for (int i = 0; i < columns.length(); i++) {
            // format: "0": "col0_name"
            //         "1": "col1_name"
            projectColumns.add(columns.getJSONObject(i).getString(String.valueOf(i)));
        }

        final AsyncKuduClient client = rest.RESTServlet.getInstance().getClient();
        final String tableName = obj.getString("TableName");
        Deferred<KuduTable> tableHook = client.openTable(obj.getString("TableName"));
//        tableHook.addCallback(new Callback<Object, KuduTable>() {
//            @Override
//            public Object call(KuduTable table) throws Exception {
//                AsyncKuduScanner scanner = client.newScannerBuilder(table)
//                        .setProjectedColumnNames(projectColumns)
//                        .build();
//
//                return null;
//            }
//        });
        KuduTable table = tableHook.join();

        AsyncKuduScanner scanner = client.newScannerBuilder(table)
                .setProjectedColumnNames(projectColumns)
                .build();

        String ret = "Ids";
        while (scanner.hasMoreRows()) {
            Deferred<RowResultIterator> resultsHook = scanner.nextRows();
            RowResultIterator results = resultsHook.join();
            while (results.hasNext()) {
                ret += ", ";
                RowResult result = results.next();
                ret += result.getInt(0);
//                System.out.println(result.getInt(0));
            }
        }
        System.out.println(ret);
        return ret;
    }
}
