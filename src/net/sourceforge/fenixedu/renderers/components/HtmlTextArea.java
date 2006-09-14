package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlTextArea extends HtmlSimpleValueComponent {

    private Integer rows;
    private Integer columns;
    private boolean readOnly;

    private String onFocus;
    private String onBlur;
    private String onSelect;
    private String onChange;
    
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

    public String getOnBlur() {
        return this.onBlur;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOnFocus() {
        return this.onFocus;
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getOnSelect() {
        return this.onSelect;
    }

    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("textarea");
        tag.removeAttribute("value");
        
        tag.setAttribute("rows", getRows());
        tag.setAttribute("cols", getColumns());
        tag.setAttribute("onfocus", getOnFocus());
        tag.setAttribute("onblur", getOnBlur());
        tag.setAttribute("onselect", getOnSelect());
        tag.setAttribute("onchange", getOnChange());
        
        if (isReadOnly()) {
            tag.setAttribute("readonly", "readonly");
        }
        
        if (isDisabled()) {
            tag.setAttribute("disabled", "disabled");
        }

        if (getValue() != null) {
            tag.setText(HtmlText.escape(getValue()));
        }
        else {
            tag.setText("");
        }
        
        return tag;
    }

}
