package rest;

import com.stumbleupon.async.Callback;
import com.stumbleupon.async.Deferred;
import model.ColumnSchemaModel;
import org.kududb.client.*;
import org.json.*;
import rest.RESTServlet;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;


/**
 * Created by zhengli.zhao on 7/7/16.
 */

@Path("insert")
public class InsertDataResource {
    @PUT
    @Consumes("application/json")
    public int InsertDataResource(String s) throws Exception {
        AsyncKuduClient client = RESTServlet.getInstance().getClient();
        final AsyncKuduSession session = client.newSession();

        JSONObject obj = new JSONObject(s);
        String tableName = obj.getString("table");

        Deferred<KuduTable> table = client.openTable(tableName);
        // set the insert as a callback to the deferred openTable
        // callBack: call-->deferred insert with callback
        //          success: insert callBack: call--> {
        //              success:
        //              fail:
        //          }
        //          fail:
        final JSONArray jsonArray = obj.getJSONArray("update");
        // TODO: figure out how to pass this jsonArray to the callback
        table.addCallback(new Callback<Object, KuduTable>() {
            // When we know that the table exists, call the callback to insert
            @Override
            public Object call(KuduTable table) throws Exception {
                Insert insert = table.newInsert();
                PartialRow row = insert.getRow();
                for (int i=0; i < jsonArray.length(); ++i) {
                    JSONObject columnObj = jsonArray.getJSONObject(i);
                    InsertDataResource.parseRowFromJson(columnObj, row);
                }
                Deferred<OperationResponse> insertHook = session.apply(insert);
                insertHook.addCallback(new Callback<Object, OperationResponse>() {
                    // After we insert, just return
                    @Override
                    public Object call(OperationResponse resp) throws Exception {
                        if (resp.hasRowError()) {
                            // if there was an error, try again?
                            // potential defaults: try 3 times
                            // have a queue of callbacks and on failure, add another one to the queue
                        }
                        else {}
                        return null;
                    }
                });
                return null;
            }
        });


        return 0;
    }

    public static void parseRowFromJson(JSONObject obj, PartialRow row) throws JSONException {
        String columnName = obj.getString("ColumnName");
        String columnType = obj.getString("ColumnType");
        switch(columnType) {
            case "INT8":
            case "INT16":
            case "INT32":
            case "INT64":
                row.addInt(columnName, obj.getInt("Value"));
                break;
            case "BOOL":
                row.addBoolean(columnName, obj.getBoolean("Value"));
                break;
            case "FLOAT":
                row.addFloat(columnName, (float) obj.getDouble("Value"));
                break;
            case "DOUBLE":
                row.addDouble(columnName, obj.getDouble("Value"));
                break;
            case "STRING":
                row.addString(columnName, obj.getString("Value"));
                break;
            case "TIMESTAMP":
            case "BINARY":
                return;
        }

    }


}