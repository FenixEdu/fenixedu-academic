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

    private String escape(String text) {
	if (text == null) {
	    return null;
	}

	return text
		.replaceAll("&", "&amp;") // must appear first
		.replaceAll("<", "&lt;")
		.replaceAll(">", "&gt;")
		.replaceAll(" ", "&nbsp;");
    }

    /**
         * @return the escaped
         */
    public boolean isEscaped() {
	return this.escaped;
    }

    /**
         * @param escaped
         *                the escaped to set
         */
    public void setEscaped(boolean escaped) {
	this.escaped = escaped;
    }
}