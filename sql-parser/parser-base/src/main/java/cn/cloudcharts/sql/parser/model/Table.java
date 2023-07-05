package cn.cloudcharts.sql.parser.model;


public class Table {

    private String catalog;
    private String db;
    private String tblName;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getTblName() {
        return tblName;
    }

    public void setTblName(String tblName) {
        this.tblName = tblName;
    }
}
