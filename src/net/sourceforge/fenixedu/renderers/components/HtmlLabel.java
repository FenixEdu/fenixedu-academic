package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlLabel extends HtmlComponent {

    private String text;
    private HtmlComponent body;
    
    private String forName;
    private String onBlur;
    private String onFocus;

    public HtmlLabel() {
        super();
    }

    public HtmlLabel(String text) {
        super();
        
        setText(text);
    }

    public String getText() {
        return this.text;
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

    public String getFor() {
        return this.forName;
    }

    public void setFor(String forName) {
        this.forName = forName;
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

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("label");
        
        tag.setAttribute("for", getFor());
        tag.setAttribute("onblur", getOnBlur());
        tag.setAttribute("onfocus", getOnFocus());
        
        if (getText() != null) {
            tag.setText(getText());
        }

        if (getBody() != null) {
            tag.addChild(getBody().getOwnTag(context));
        }
        
        return tag;
    }
    
}
