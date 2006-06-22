package net.sourceforge.fenixedu.renderers.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlTableCell extends HtmlComponent {

    public enum CellType {
        DATA,
        HEADER;
        
        public String toString() {
            String[] names = new String[] { "td", "th" };
            
            return names[this.ordinal()];
        }
    }

    private CellType type;
    
    private HtmlComponent data;

    private String abbr;
    private String axis;
    private String headers;
    private String scope;
    private Integer rowspan;
    private Integer colspan;
    private String align;
    private String valign;
    
    public HtmlTableCell() {
        this(CellType.DATA);
    }
    
    public HtmlTableCell(String text) {
        this();
        
        setText(text);
    }
    
    public HtmlTableCell(CellType header) {
        this.type = header;
    }

    public CellType getType() {
        return type;
    }

    public void setType(CellType type) {
        this.type = type;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAlign() {
        return align;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getValign() {
        return valign;
    }

    public void setValign(String valign) {
        this.valign = valign;
    }

    public void setText(String text) {
        this.data = new HtmlText(text);
    }

    public void setBody(HtmlComponent body) {
        data = body;
    }
    
    public HtmlComponent getBody() {
        return data;
    }
    
    @Override
    public List<HtmlComponent> getChildren() {
        List<HtmlComponent> children = new ArrayList<HtmlComponent>(super.getChildren());
        
        if (getBody() != null) {
            children.add(getBody());
        }
        
        return children;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName(type.toString());
        
        tag.setAttribute("abbr", getAbbr());
        tag.setAttribute("axis", getAxis());
        tag.setAttribute("headers", getHeaders());
        tag.setAttribute("scope", getScope());
        tag.setAttribute("rowspan", getRowspan());
        tag.setAttribute("colspan", getColspan());
        tag.setAttribute("align", getAlign());
        tag.setAttribute("valign", getValign());
        
        if (data != null) {
            tag.addChild(this.data.getOwnTag(context));
        }
        
        return tag;
    }
}
