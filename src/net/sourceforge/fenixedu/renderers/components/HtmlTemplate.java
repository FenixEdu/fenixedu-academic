package net.sourceforge.fenixedu.renderers.components;

import java.io.IOException;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlIncludeTag;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

/**
 * This component is intended to as an abstraction to a certain page fragment.
 * 
 * The {@link #draw(PageContext)} method is defined so that that fragment will
 * be included in the place this component is used.
 * 
 * @author cfgi
 */
public class HtmlTemplate extends HtmlComponent {
    private String template;

    transient private PageContext context;
    transient private Object object;
    transient private Object oldObject;
    
    public HtmlTemplate(String template, Object object) {
        this.template = template;
        this.object = object;
    }

    @Override
    public void draw(PageContext context) throws IOException {
        this.context = context;
        
        // TODO: find how to use the PAGE_SCOPE of the included page
        // because the context of the page is different from the including
        // context

        pushPageEnvironment(context);
        
        context.getOut().flush();
        super.draw(context);
        
        popPageEnvironment(context);
    }

    private void pushPageEnvironment(PageContext context) {
        oldObject = context.getAttribute(Constants.TEMPLATE_OBJECT_NAME, PageContext.REQUEST_SCOPE);

        setPageEnvironment(context, object);
    }
    
    private void popPageEnvironment(PageContext context) {
        setPageEnvironment(context, oldObject);
    }

    private void setPageEnvironment(PageContext context, Object object) {
        context.setAttribute(Constants.TEMPLATE_OBJECT_NAME, object, PageContext.REQUEST_SCOPE);
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        return new HtmlIncludeTag(tag, context, template);
    }
}
