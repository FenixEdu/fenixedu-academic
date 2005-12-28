package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlTextInput extends HtmlInputComponent {

    private Boolean readOnly;
    
    private Integer maxLength;
    
    public HtmlTextInput() {
        super("text");
    }
    
    protected HtmlTextInput(String type) {
        super(type);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Boolean getReadOnly() {
        return readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly = readOnly;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag =  super.getOwnTag(context);
        
        tag.setAttribute("maxlength", this.maxLength);
        
        return tag;
    }
}
