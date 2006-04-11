package net.sourceforge.fenixedu.renderers.components;

public class HtmlTableHeaderRow extends HtmlTableRow {
    public HtmlTableCell createCell() {
        HtmlTableCell cell = new HtmlTableCell(HtmlTableCell.CellType.HEADER);
        
        cell.setScope("col");

        addCell(cell);
        return cell;
    }
}
