package net.sourceforge.fenixedu.renderers.components;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlIncludeTag;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

/**
 * This component is intended to as an abstraction to a certain page fragment.
 * 
 * @author cfgi
 */
public class HtmlTemplate extends HtmlComponent {
    private String template;
    transient private Object object;
    
    public HtmlTemplate(String template, Object object) {
        this.template = template;
        this.object = object;
    }

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        Map<String, Object> environment = new HashMap<String, Object>();
        environment.put(Constants.TEMPLATE_OBJECT_NAME, this.object);
        
        return new HtmlIncludeTag(context, template, environment);
    }
}
