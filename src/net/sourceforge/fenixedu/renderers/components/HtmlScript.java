package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public class HtmlScript extends HtmlComponent {

    private String charset;
    private String contentType;
    private String source;
    private boolean defer;
    private boolean conditional;

    private CharSequence script;
    
    public HtmlScript() {
        super();
    }

    public HtmlScript(String contentType, String source) {
        super();
    
        this.contentType = contentType;
        this.source = source;
    }

    public HtmlScript(String contentType, String source, boolean conditional) {
        this(contentType, source);
    
        this.conditional = conditional;
    }

    public String getCharset() {
        return this.charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getContentType() {
        return this.contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isDefer() {
        return this.defer;
    }

    public void setDefer(boolean defer) {
        this.defer = defer;
    }

    public boolean isConditional() {
        return conditional;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CharSequence getScript() {
        return this.script;
    }

    public void setScript(CharSequence script) {
        this.script = script;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);

        if (isConditional() && wasIncluded(context)) {
            tag.setName(null);
            return tag;
        }
        
        tag.setName("script");
        tag.setAttribute("charset", getCharset());
        tag.setAttribute("type", getContentType());
        tag.setAttribute("src", getSource());
        
        if (isDefer()) {
            tag.setAttribute("defer", "defer");
        }

        if (getScript() != null) {
            tag.setText(getScript().toString());
        }
        
        return tag;
    }

    private boolean wasIncluded(PageContext context) {
        if (getSource() == null) {
            return false;
        }

        ServletRequest request = context.getRequest();
        String sourceName = getClass().getName() + "/included/" + getSource();
        
        if (request.getAttribute(sourceName) != null) {
            return true;
        }
        else {
            request.setAttribute(sourceName, true);
            return false;
        }
    }
}
