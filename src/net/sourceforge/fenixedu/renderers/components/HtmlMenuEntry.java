package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public abstract class HtmlMenuEntry extends HtmlComponent {

    private String label;
    private Boolean disabled;
    
    public HtmlMenuEntry(String label, Boolean disabled) {
        super();
        
        this.label = label;
        this.disabled = disabled;
    }

    public Boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setAttribute("label", this.label);
        tag.setAttribute("disabeld", this.disabled);
        
        return tag;
    }
    
    public abstract void setSelected(String value);
}
