package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMenuOption extends HtmlMenuEntry {

    private boolean selected;

    private String value;
    
    private String text;
    
    public HtmlMenuOption() {
        super(null, null);
    }

    public HtmlMenuOption(String text) {
        super(null, null);
        
        this.text = text;
    }

    public HtmlMenuOption(String text, String value) {
        this(text);
        
        this.value = value;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public void setSelected(String value) {
        setSelected(getValue() != null && getValue().equals(value));
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);

        tag.setName("option");
    
        tag.setText(getText());
        
        if (this.selected) {
            // XHTML 1.0 Transaction requires the selected attribute to have the value "selected"
            tag.setAttribute("selected", "selected");
        }
        
        tag.setAttribute("value", getValue() == null ? getText() : getValue());
        
        return tag;
    }
    
}
