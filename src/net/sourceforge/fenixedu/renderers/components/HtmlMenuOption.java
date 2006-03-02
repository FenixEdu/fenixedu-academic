package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlMenuOption extends HtmlMenuEntry {

    private boolean selected;

    private String value;
    
    private String text;
    
    private HtmlComponent body;
    
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

    public HtmlMenuOption(HtmlComponent body) {
        super(null, null);
        
        this.body = body;
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

    public HtmlComponent getBody() {
        return this.body;
    }

    public void setBody(HtmlComponent body) {
        this.body = body;
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
    
        if (getText() != null) {
            tag.setText(getText());
        }
        
        if (this.selected) {
            // XHTML 1.0 Transaction requires the selected attribute to have the value "selected"
            tag.setAttribute("selected", "selected");
        }
        
        tag.setAttribute("value", getValue() == null ? getText() : getValue());
        
        if (getBody() != null) {
            tag.addChild(getBody().getOwnTag(context));
        }
        
        return tag;
    }
    
}
