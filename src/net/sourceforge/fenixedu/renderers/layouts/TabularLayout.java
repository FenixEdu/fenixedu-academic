package net.sourceforge.fenixedu.renderers.layouts;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableHeader;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;

import org.apache.log4j.Logger;

public abstract class TabularLayout extends Layout {
    private static Logger logger = Logger.getLogger(TabularLayout.class);

    private String caption;

    private String rowClasses;

    private String columnClasses;

    private String headerClasses;

    private HtmlTable table;
    
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
    }

    public void setHeaderClasses(String headerClasses) {
        this.headerClasses = headerClasses;
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
            HtmlTableRow row = header.createRow();

            for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
                HtmlTableCell cell = row.createCell();

                cell.setBody(getHeaderComponent(columnIndex));
            }
        }

        for (int rowIndex = 0; rowIndex < rowNumber; rowIndex++) {
            HtmlTableRow row = table.createRow();

            try {
                for (int columnIndex = 0; columnIndex < columnNumber; columnIndex++) {
                    HtmlTableCell cell = row.createCell();

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

    protected abstract int getNumberOfColumns();

    protected abstract int getNumberOfRows();

    protected abstract HtmlComponent getHeaderComponent(int columnIndex);

    protected abstract HtmlComponent getComponent(int rowIndex, int columnIndex);
    
    @Override
    public void applyStyle(HtmlComponent component) {
        super.applyStyle(component);

        HtmlTable table = (HtmlTable) component;

        table.setCaption(this.caption);

        // header
        HtmlTableHeader header = table.getHeader();
        if (header != null) {
            header.setClasses(this.headerClasses);
        }

        // decompose row and cell classes
        String[] rowClasses = null;
        if (this.rowClasses != null) {
            rowClasses = this.rowClasses.split(",", -1);
        }

        String[] cellClasses = null;
        if (this.columnClasses != null) {
            cellClasses = this.columnClasses.split(",", -1);
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

        // footer
    }
}
