package net.sourceforge.fenixedu.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Created on Nov 23, 2003
 *  
 */

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class TableInfo {
    private String tableName;

    private String primaryKey;

    private String columns;

    private List idsToSelect;

    /**
     * Default Constructor
     *  
     */
    public TableInfo() {

    }

    /**
     * @param tableName
     * @param primaryKey
     * @param columns
     */
    public TableInfo(String tableName, String primaryKey, String columns) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.idsToSelect = new ArrayList();
    }

    /**
     * @param tableName
     * @param primaryKey
     * @param columns
     * @param idsToSelect
     */
    public TableInfo(String tableName, String primaryKey, String columns, List idsToSelect) {
        this.tableName = tableName;
        this.primaryKey = primaryKey;
        this.columns = columns;
        this.idsToSelect = idsToSelect;
    }

    /**
     * @return Returns the columns.
     */
    public String getColumns() {
        return columns;
    }

    /**
     * @param columns
     *            The columns to set.
     */
    public void setColumns(String columns) {
        this.columns = columns;
    }

    /**
     * @return Returns the primaryKey.
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey
     *            The primaryKey to set.
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * @return Returns the tableName.
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     *            The tableName to set.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return Returns the idsToSelect.
     */
    public List getIdsToSelect() {
        return idsToSelect;
    }

    /**
     * @param idsToSelect
     *            The idsToSelect to set.
     */
    public void setIdsToSelect(List idsToSelect) {
        this.idsToSelect = idsToSelect;
    }

    /**
     * Adds a new id to select
     * 
     * @param id
     */
    public void addIdToSelect(Integer id) {
        if (this.idsToSelect.contains(id) == false) {
            this.idsToSelect.add(id);
        }
    }

    /**
     * Intersects the two lists of ids to select, keeping only diferent ids
     * 
     * @param idsToSelect
     */
    public void IntersectIdsToSelect(List idsToSelect) {
        for (Iterator iter = idsToSelect.iterator(); iter.hasNext();) {
            Integer id = (Integer) iter.next();
            if (this.idsToSelect.contains(id) == false) {
                this.idsToSelect.add(id);
            }
        }
    }

    public String toSql() {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT " + this.columns);
        sb.append(" FROM " + this.tableName);

        if (this.idsToSelect.size() <= 0) {
            return sb.toString();
        }

        sb.append(" WHERE " + this.primaryKey + " IN (");
        for (int i = 0; i < this.idsToSelect.size(); i++) {
            Integer id = (Integer) this.idsToSelect.get(i);
            sb.append(id);
            if (i != (this.idsToSelect.size() - 1)) {
                sb.append(",");
            }
        }
        sb.append(")");

        return sb.toString();
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof TableInfo) {
            TableInfo tableInfo = (TableInfo) obj;
            if ((this.columns.equals(tableInfo.getColumns()))
                    && (this.tableName.equals(tableInfo.getTableName()))
                    && (this.primaryKey.equals(tableInfo.getPrimaryKey()))
                    && (this.idsToSelect.equals(tableInfo.getIdsToSelect()))) {
                result = true;
            }
        }

        return result;
    }

}