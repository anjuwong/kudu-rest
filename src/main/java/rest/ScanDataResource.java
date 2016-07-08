package rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.kududb.client.KuduClient;
import org.kududb.client.KuduScanner;
import org.kududb.client.RowResult;
import org.kududb.client.RowResultIterator;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import java.util.ArrayList;


@Path("scan")
public class ScanDataResource {
    @GET
    @Produces("application/json")
    public String scan(String s) throws Exception {
        ArrayList<String> projectColumns = new ArrayList<String>(1);
        KuduClient client = rest.RESTServlet.getInstance().getClient();
        JSONObject obj = new JSONObject(s);
        String table = obj.getString("TableName");
        JSONArray columns = obj.getJSONArray("Columns");
        for (int i = 0; i < columns.length(); i++) {
            projectColumns.add(columns.getJSONObject(i).getString(String.valueOf(i)));
        }
        KuduScanner scanner = client.newScannerBuilder(client.openTable(table))
                .setProjectedColumnNames(projectColumns)
                .build();
        String ret = "Ids";
        while (scanner.hasMoreRows()) {
            RowResultIterator results = scanner.nextRows();
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
