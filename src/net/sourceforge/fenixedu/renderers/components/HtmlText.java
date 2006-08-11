package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlText extends HtmlComponent {

    private String text;
    private boolean escaped;

    public HtmlText(String text, boolean escaped) {
        this.text = text;
        this.escaped = escaped;
    }

    public HtmlText(String text) {
        this.text = text;
        this.escaped = true;
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

    public boolean isEscaped() {
        return this.escaped;
    }

    public void setEscaped(boolean escaped) {
        this.escaped = escaped;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        	HtmlTag tag = super.getOwnTag(context);
        
        	if (tag.hasVisibleAttributes()) {
        	    tag.setName("span");
        	} else {
        	    tag.setName(null);
        	}
        
        	if (this.escaped) {
        	    tag.setText(escape(this.text));
        	} else {
        	    tag.setText(this.text);
        	}
        
        	return tag;
    }

    public static String escape(String text) {
        	if (text == null) {
        	    return null;
        	}
        
        	return text
        		.replaceAll("&", "&amp;") // must appear first
        		.replaceAll("<", "&lt;")
        		.replaceAll(">", "&gt;")
        		.replaceAll("  ", " &nbsp;") // TODO: check this
            .replaceAll("\"", "&quot;");
    }
}