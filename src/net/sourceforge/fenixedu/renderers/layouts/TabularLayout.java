package net.sourceforge.fenixedu.renderers.layouts;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableHeader;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;

import org.apache.log4j.Logger;

public abstract class TabularLayout extends Layout {
    private static Logger logger = Logger.getLogger(TabularLayout.class);

    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private HtmlTable table;
    
    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColumnClasses() {
        return this.columnClasses;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public String getHeaderClasses() {
        return this.headerClasses;
    }

    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
    }

    public String getRowClasses() {
        return this.rowClasses;
    }

    public void setRowClasses(String rowClasses) {
        this.rowClasses = rowClasses;
    }

    public HtmlTable getTable() {
        return this.table;
    }

    public void setTable(HtmlTable table) {
        this.table = table;
    }

    @Override
    public String[] getPropertyNames() {
        return mergePropertyNames(super.getPropertyNames(), new String[] { "caption", "rowClasses",
                "columnClasses", "headerClasses" });
    }

    @Override
    public HtmlComponent createComponent(Object object, Class type) {
        HtmlTable table = new HtmlTable();
        setTable(table);

        int rowNumber = getNumberOfRows();
        int columnNumber = getNumberOfColumns();

        if (hasHeader()) {
            HtmlTableHeader header = table.createHeader();
            
            HtmlTableRow firstRow = header.createRow();
            HtmlTableRow secondRow = null;

            if (hasHeaderGroups()) {
                secondRow = header.createRow();
            }

            String lastGroup = null;
            HtmlTableCell lastGroupCell = null;
            
            for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
                String group = getHeaderGroup(columnIndex);
                
                if (hasHeaderGroups() && group != null) {
                    if (lastGroup != null && lastGroup.equals(group)) {
                        if (lastGroupCell.getColspan() == null) {
                            lastGroupCell.setColspan(2);
                        }
                        else {
                            lastGroupCell.setColspan(lastGroupCell.getColspan() + 1);
                        }
                    }
                    else {
                        HtmlTableCell cell = firstRow.createCell();
                        cell.setBody(new HtmlText(group));
                        
                        lastGroup = group;
                        lastGroupCell = cell;
                    }
                    
                    HtmlTableCell cell = secondRow.createCell();
                    cell.setBody(getHeaderComponent(columnIndex));
                }
                else {
                    lastGroup = null;
                    lastGroupCell = null;
                    
                    HtmlTableCell cell = firstRow.createCell();
                    cell.setBody(getHeaderComponent(columnIndex));
                    
                    if (hasHeaderGroups()) {
                        cell.setRowspan(2);
                    }
                }
            }
        }

        for (int rowIndex = 0; rowIndex < rowNumber; rowIndex++) {
            HtmlTableRow row = table.createRow();

            try {
                for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
                    HtmlTableCell cell = row.createCell();

                    if (isHeader(rowIndex, columnIndex)) {
                        cell.setType(HtmlTableCell.CellType.HEADER);
                    }

                    costumizeCell(cell, rowIndex, columnIndex);
                    if (cell.getColspan() != null) {
                        columnIndex += cell.getColspan() - 1;
                    }
                    
                    cell.setBody(getComponent(rowIndex, columnIndex));
                }
            } catch (Exception e) {
                logger.warn("while generating table row " + rowIndex + " catched exception " + e);
                e.printStackTrace();
                table.removeRow(row);
            }
        }

        return table;
    }

    protected boolean hasHeader() {
        return false;
    }

    protected boolean hasHeaderGroups() {
        return false;
    }
    
    protected abstract HtmlComponent getHeaderComponent(int columnIndex);

    protected String getHeaderGroup(int columnIndex) {
        return null;
    }

    protected void costumizeCell(HtmlTableCell cell, int rowIndex, int columnIndex) {
        
    }
    
    protected abstract int getNumberOfColumns();

    protected abstract int getNumberOfRows();

    protected abstract HtmlComponent getComponent(int rowIndex, int columnIndex);
    
    protected boolean isHeader(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public void applyStyle(HtmlComponent component) {
        super.applyStyle(component);

        HtmlTable table = (HtmlTable) component;
       
        table.setCaption(this.caption);

        // header
        HtmlTableHeader header = table.getHeader();
        if (header != null) {
            // Is not very specific
            // header.setClasses(getHeaderClasses());

            for (HtmlTableRow row : header.getRows()) {
                for (HtmlTableCell cell : row.getCells()) {
                    cell.setClasses(getHeaderClasses());
                }
            }
        }

        // decompose row and cell classes
        String[] rowClasses = null;
        if (getRowClasses() != null) {
            rowClasses = getRowClasses().split(",", -1);
        }

        String[] cellClasses = null;
        if (getColumnClasses() != null) {
            cellClasses = getColumnClasses().split(",", -1);
        }

        // check if additional styling is needed
        if (rowClasses == null && cellClasses == null) {
            return;
        }

        // apply style by rows and columns
        int rowIndex = 0;
        for (HtmlTableRow row : table.getRows()) {
            if (rowClasses != null) {
                String chooseRowClass = rowClasses[rowIndex % rowClasses.length];
                if (!chooseRowClass.equals("")) {
                    row.setClasses(chooseRowClass);
                }
            }

            if (cellClasses != null) {
                int cellIndex = 0;
                for (HtmlTableCell cell : row.getCells()) {
                    String chooseCellClass = cellClasses[cellIndex % cellClasses.length];
                    if (!chooseCellClass.equals("")) {
                        cell.setClasses(chooseCellClass);
                    }

                    cellIndex++;
                }
            }

            rowIndex++;
        }
    }
    
}