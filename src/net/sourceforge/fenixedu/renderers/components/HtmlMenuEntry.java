package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public abstract class HtmlMenuEntry extends HtmlComponent {

    private String label;
    private boolean disabled;
    
    public HtmlMenuEntry(String label, boolean disabled) {
        super();
        
        this.label = label;
        this.disabled = disabled;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
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
        
        if (isDisabled()) {
            tag.setAttribute("disabled", "disabled");
        }
        
        return tag;
    }
    
    public abstract void setSelected(String value);
    
    public abstract boolean isSelected();
}
