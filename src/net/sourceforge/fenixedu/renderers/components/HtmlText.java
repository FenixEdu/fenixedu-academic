package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlText extends HtmlComponent {

    private String text;

    public HtmlText(String text) {
        this.text = text;
    }

    public HtmlText() {
        this.text = "";
    }
    
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        if (tag.hasVisibleAttributes()) {
            tag.setName("span");
        }
        else {
            tag.setName(null);
        }
        
        tag.setText(this.text);
        
        return tag;
    }
}
