package net.sourceforge.fenixedu.renderers.components.tags;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

public class HtmlIncludeTag extends HtmlTag {

    private PageContext context;
    private String template;
    
    private Map<String, Object> savedValues;
    private Map<String, Object> usedAttributes;

    public HtmlIncludeTag(PageContext context, String template, Map<String, Object> attributes) {
        super(null);
        
        this.context = context;
        this.template = template;
        
        this.usedAttributes = attributes;
        this.savedValues = new HashMap<String, Object>();
    }

    private void pushAttributes() {
        if (usedAttributes == null) {
            return;
        }
        
        for (String name : usedAttributes.keySet()) {
            Object value = context.getAttribute(name, PageContext.REQUEST_SCOPE);
            savedValues.put(name, value);
            
            context.setAttribute(name, usedAttributes.get(name), PageContext.REQUEST_SCOPE);
        }
    }
    
    private void popAttributes() {
        if (usedAttributes == null) {
            return;
        }

        for (String name : savedValues.keySet()) {
            context.setAttribute(name, savedValues.get(name), PageContext.REQUEST_SCOPE);
        }
    }

    @Override
    public void writeTag(Writer writer, String indent) throws IOException {
        pushAttributes();

        try {
            context.getOut().flush();
            context.include(template);
        } catch (ServletException e) {
            e.printStackTrace();
        }
        
        popAttributes();
    }
}
