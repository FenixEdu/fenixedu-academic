package net.sourceforge.fenixedu.renderers.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class FormTag extends ContextTag {

    private String action;
    private String encoding;
    
    public String getAction() {
        return this.action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    @Override
    public int doStartTag() throws JspException {
        writeStartForm();
        
        return super.doStartTag();
    }
    
    @Override
    public int doEndTag() throws JspException {
        super.doEndTag();
        writeEndForm();

        return EVAL_PAGE;
    }
    
    private void writeStartForm() throws JspException {
        StringBuilder formHead = new StringBuilder();
        
        formHead.append("<form ");
        if (getId() != null) {
            formHead.append("id=\"" + getId() + "\" ");
        }
        
        String path = RenderUtils.getModuleRelativePath(getAction());
        formHead.append("action=\"" + path + "\" ");
        
        if (getEncoding() != null) {
            formHead.append("enctype=\"" + getEncoding() + "\" ");
        }
        
        formHead.append("method=\"post\">\n");
        
        try {
            pageContext.getOut().write(formHead.toString());
        } catch (IOException e) {
            throw new JspException("could not generate form");
        }
    }

    private void writeEndForm() throws JspException {
        StringBuilder formTail = new StringBuilder();
        
        formTail.append("</form>");
        
        try {
            pageContext.getOut().write(formTail.toString());
        } catch (IOException e) {
            throw new JspException("could not generate form");
        }
    }

}
