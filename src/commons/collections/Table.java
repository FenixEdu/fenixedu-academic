/*
 * @(#)Table.java Created on Nov 29, 2004
 *
 */
package commons.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carla Penedo <carla.penedo@ist.utl.pt>
 * 
 */
public class Table {

    private ArrayList columns;

    private int numberRows = 0;

    public Table(int columnNr) {
        super();
        columns = new ArrayList(columnNr);
        for (int i = 0; i < columnNr; i++) {
            columns.add(i, new ArrayList());
        }
    }

    public void put(int rowIndex, int columnIndex, Object object) {
        ArrayList column = (ArrayList) columns.get(columnIndex);
        fill(column, rowIndex-1);
        column.add(rowIndex, object);
        if (column.size() > numberRows) {
            numberRows = column.size();
        }
    }

    public void appendToColumn(int columnIndex, Object object) {
       ArrayList column = (ArrayList) columns.get(columnIndex);
       put(column.size(), columnIndex, object);
    }

    public Object get(int rowIndex, int columnIndex) {
        ArrayList column = (ArrayList) columns.get(columnIndex);
        if (rowIndex < column.size()) { return column.get(rowIndex); }
        return "";
    }

    public List getRow(int rowIndex) {
        List row = new ArrayList(columns.size());

        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            row.add(columnIndex, get(rowIndex, columnIndex));
        }

        return row;
    }

    /**
     * @return Returns the numberColumns.
     */
    public int getNumberColumns() {
        return columns.size();
    }

    /**
     * @return Returns the numberRows.
     */
    public int getNumberRows() {
        return numberRows;
    }

    /**
     * @param list
     * @param rowIndex
     */
    private void fill(ArrayList list, int rowIndex) {
        while (list.size() <= rowIndex) {
            list.add(null);
        }
    }
}
