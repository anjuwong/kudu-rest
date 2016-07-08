package rest;

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
        KuduClient client = RESTServlet.getInstance().getClient();
        KuduSession session = client.newSession();

        JSONObject obj = new JSONObject(s);
        String tableName = obj.getString("table");
        KuduTable table = client.openTable(tableName);

        JSONArray jsonArray = obj.getJSONArray("update");

        Insert insert = table.newInsert();
        PartialRow row = insert.getRow();
        for (int i=0; i < jsonArray.length(); ++i) {
            JSONObject columnObj = jsonArray.getJSONObject(i);
            InsertDataResource.parseRowFromJson(columnObj, row);
        }
        session.apply(insert);

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