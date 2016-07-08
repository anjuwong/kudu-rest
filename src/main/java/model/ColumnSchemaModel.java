package model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
//import org.codehaus.jackson.annotate.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by andrew.wong on 7/7/16.
 */
@XmlRootElement(name="ColumnSchema")
public class ColumnSchemaModel implements Serializable {

    public ColumnSchemaModel() {}

    public ColumnSchemaModel(final String columnName) {
        this(columnName.getBytes());
        }

    public ColumnSchemaModel(final byte[] columnName) {
        this.ColumnName = columnName;
        }

    public ColumnSchemaModel(final byte[] columnName, final byte[] columnType) {
        this.ColumnName = columnName;
        this.ColumnType = columnType;
        }

    public ColumnSchemaModel(final String columnName, final String columnType) {
        this.ColumnName = columnName.getBytes();
        this.ColumnType = columnType.getBytes();
        }

    public ColumnSchemaModel(final byte[] columnName, final String columnType) {
        this.ColumnName = columnName;
        this.ColumnType = columnType.getBytes();
        }

    public ColumnSchemaModel(final String columnName, final byte[] columnType) {
        this.ColumnName = columnName.getBytes();
        this.ColumnType = columnType;
        }

    public byte[] getColumnName() {
        return this.ColumnName;
        }

    public void setColumnName(byte[] columnName) {
        this.ColumnName = columnName;
        }

    public byte[] getColumnType() {
        return this.ColumnType;
        }

    public void setColumnType(byte[] columnType) {
        this.ColumnType = columnType;
        }

//    @JsonProperty("ColumnName")
//    @XmlAttribute
    private byte[] ColumnName;

//    @JsonProperty("ColumnType")
//    @XmlAttribute
    private byte[] ColumnType;

}