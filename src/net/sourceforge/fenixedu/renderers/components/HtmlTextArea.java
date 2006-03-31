package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlTextArea extends HtmlSimpleValueComponent {

    private Integer rows;
    private Integer columns;
    private boolean readOnly;
    
    public Integer getColumns() {
        return this.columns;
    }

    public void setColumns(Integer colums) {
        this.columns = colums;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getRows() {
        return this.rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("textarea");
        tag.removeAttribute("value");
        
        tag.setAttribute("rows", getRows());
        tag.setAttribute("cols", getColumns());
        
        if (isReadOnly()) {
            tag.setAttribute("readonly", "readonly");
        }
        
        if (isDisabled()) {
            tag.setAttribute("disabled", "disabled");
        }

        if (getValue() != null) {
            tag.setText(getValue());
        }
        else {
            tag.setText("");
        }
        
        return tag;
    }

}
