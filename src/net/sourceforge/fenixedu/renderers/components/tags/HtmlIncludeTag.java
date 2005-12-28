package net.sourceforge.fenixedu.renderers.components.tags;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;


public class HtmlIncludeTag extends HtmlTag {

    private PageContext context;
    private String template;

    public HtmlIncludeTag(HtmlTag tag, PageContext context, String template) {
        super(null);
        
        this.context = context;
        this.template = template;
        
        copyAttributes(tag);
    }

    @Override
    public void writeTag(Writer writer, String indent) throws IOException {
        try {
            context.include(template);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
