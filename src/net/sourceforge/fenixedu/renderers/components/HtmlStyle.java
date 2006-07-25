package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlStyle extends HtmlComponent {
    private String type;
    private String media;
    private String text;
    
    private CharSequence styleBody;

    public HtmlStyle() {
        super();
        
        setType("text/css");
    }

    public String getMedia() {
        return this.media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public CharSequence getStyleBody() {
        return this.styleBody;
    }

    public void setStyleBody(CharSequence styleBody) {
        this.styleBody = styleBody;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("style");
        tag.setAttribute("type", getType());
        tag.setAttribute("media", getMedia());
        tag.setAttribute("text", getText());
        
        if (getStyleBody() != null) {
            tag.setText(getStyleBody().toString());
        }
        
        return tag;
    }
    
}
