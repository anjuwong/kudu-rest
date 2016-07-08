package rest;

import model.ColumnSchemaModel;
import org.kududb.ColumnSchema;
import org.kududb.Common;
import org.kududb.Schema;
import org.kududb.Type;
import org.kududb.client.*;
import org.json.*;
import rest.RESTServlet;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;
//import java.nio.charset.StandardCharsets;

/**
 * Created by andrew.wong on 7/7/16.
 */
@Path("createtable")
public class CreateTableResource {
    // TODO: LEARN HOW TO PROTOBUF AND HOW TO JERSEY
    // TODO: error handling
    @PUT
    @Consumes("application/json")
//    public int insert(ColumnSchemaModel model) throws Exception {
    public int createTable(String s) throws Exception {
        KuduClient client = RESTServlet.getInstance().getClient();
        JSONObject obj = new JSONObject(s);
        System.out.println(s);
        Schema schema = CreateTableResource.getSchemaFromJson(obj);

        client.createTable(obj.getString("TableName"),
                CreateTableResource.getSchemaFromJson(obj),
                new CreateTableOptions().setRangePartitionColumns(
                        CreateTableResource.getRangeKeysFromJson(obj)
                ));
        return 0;
    }

    public static List<String> getRangeKeysFromJson(JSONObject obj) throws Exception {
        JSONArray rangeKeyArray = obj.getJSONArray("RangeKeys");
        List<String> rangeKeys = new ArrayList<String>();
        for (int i = 0; i < rangeKeyArray.length(); i++) {
            JSONObject keyStruct = rangeKeyArray.getJSONObject(i);
            rangeKeys.add(keyStruct.getString("Key"));
        }
        return rangeKeys;
    }

    public static Schema getSchemaFromJson(JSONObject obj) throws Exception {
        String tableName = obj.getString("TableName");
        JSONArray columnArray = obj.getJSONArray("Columns");
        List<ColumnSchema> columns = new ArrayList();
        for (int i = 0; i < columnArray.length(); i++) {

            // Get values from JSON
            JSONObject col = columnArray.getJSONObject(i);
            String name = col.getString("ColumnName");
            Type type = CreateTableResource.getType(col.getString("ColumnType"));
            boolean keyBool = col.getBoolean("Key");

            // Build the proper columns
            columns.add(new ColumnSchema.ColumnSchemaBuilder(name, type)
                        .key(keyBool)
                        .build());
        }
        return new Schema(columns);

    }
    public static Type getType(String type) {
        if (type.equals("INT8")) {
            return Type.INT8;
        } else if (type.equals("INT16")) {
            return Type.INT16;
        } else if (type.equals("INT32")) {
            return Type.INT32;
        } else if (type.equals("INT64")) {
            return Type.INT64;
        } else if (type.equals("BINARY")) {
            return Type.BINARY;
        } else if (type.equals("STRING")) {
            return Type.STRING;
        } else if (type.equals("BOOL")) {
            return Type.BOOL;
        } else if (type.equals("FLOAT")) {
            return Type.FLOAT;
        } else if (type.equals("DOUBLE")) {
            return Type.DOUBLE;
        } else if (type.equals("TIMESTAMP")) {
            return Type.TIMESTAMP;
        } else {
            return Type.INT8;
        }
    }
}