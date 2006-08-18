package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlText extends HtmlComponent {

    private String text;
    private boolean escaped;
    private boolean newLineAware;
    
    public HtmlText(String text, boolean escaped) {
        this.text = text;
        this.escaped = escaped;
        this.newLineAware = true;
    }

    public HtmlText(String text, boolean escaped, boolean newLineAware) {
        this.text = text;
        this.escaped = escaped;
        this.newLineAware = newLineAware;
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
        
        String finalText = this.escaped ? escape(this.text) : this.text;
        finalText = this.newLineAware ? replaceNewlines(finalText) : finalText;
        
        tag.setText(finalText);
        
        	return tag;
    }

    private String replaceNewlines(String string) {
        StringBuilder result = new StringBuilder();
        
        String[] lines = string.split("\\r\\n|\\n|\\r", -1);
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];

            if (i > 0) {
                result.append("<br/>");
            }
            
            result.append(line);
        }
        
        return result.toString();
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