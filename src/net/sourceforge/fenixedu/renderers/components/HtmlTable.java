package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlTable extends HtmlComponent {

    private List<HtmlTableRow> rows;

    private HtmlTableHeader header;

    private String summary;
    private String width; // can be "100%"
    private String border;
    private String cellSpacing;
    private String cellPadding;
    private String caption;
    
    // frame and rules were ignored

    public HtmlTable() {
        rows = new ArrayList<HtmlTableRow>();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getWidth() {
        return width;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String border) {
        this.border = border;
    }

    public String getCellPadding() {
        return cellPadding;
    }

    public void setCellPadding(String cellPadding) {
        this.cellPadding = cellPadding;
    }

    public String getCellSpacing() {
        return cellSpacing;
    }

    public void setCellSpacing(String cellSpacing) {
        this.cellSpacing = cellSpacing;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public HtmlTableRow createRow() {
        HtmlTableRow row = new HtmlTableRow();

        this.rows.add(row);
        
        return row;
    }

    public void removeRow(HtmlTableRow row) {
        this.rows.remove(row);
    }

    public List<HtmlTableRow> getRows() {
        return rows;
    }
    
    public HtmlTableHeader createHeader() {
        this.header = new HtmlTableHeader();

        return this.header;
    }

    public HtmlTableHeader getHeader() {
        return header;
    }
    
    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        if (this.header != null) {
            children.add(this.header);
        }
        
        children.addAll(this.rows);
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        
    	HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("table");
        
        tag.setAttribute("summary", summary);
        tag.setAttribute("width", width);
        tag.setAttribute("border", border);
        tag.setAttribute("cellSpacing", cellSpacing);
        tag.setAttribute("cellPadding", cellPadding);
        
        if (this.caption != null) {
            tag.addChild(new HtmlTag("caption", this.caption));
        }
        
        if (this.header != null) {
            for (HtmlTableRow row : header.getRows()) {
                tag.addChild(row.getOwnTag(context));
            }
            //tag.addChild(header.getOwnTag(context));
        }
        
        if (this.rows.size() >= 0) {
            for (HtmlTableRow row : this.rows) {
                tag.addChild(row.getOwnTag(context));
            }
        }
        else {
            HtmlTag innerTag = new HtmlTag("tbody");
            tag.addChild(innerTag);
        }
        
        // Always generate end tag
        if (tag.getChildren().isEmpty()) {
            tag.addChild(new HtmlTag(null));
        }
        
        return tag;
    }

}
