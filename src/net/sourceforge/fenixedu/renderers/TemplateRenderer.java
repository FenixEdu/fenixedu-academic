package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTemplate;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

/**
 * This renderer allows you to delegate the presentation of an object to 
 * a JSP page. This can be usefull if you want to incrementally replace
 * some page with the use of renderers, use some of the functionalities 
 * available in the JSP template and that are provided by this
 * renderer and the <code>renderers-template.tld</code>, or present a value
 * in a complex and less orthodox way.
 * 
 * @author cfgi
 */
public class TemplateRenderer extends OutputRenderer {

    private String template;
    
	public String getTemplate() {
        return template;
    }

    /**
     * The location of the template page that ill be used to present the
     * value. This location if relative to the application context but must
     * begin with a /.
     * 
     * @property
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                return new HtmlTemplate(getTemplate(), getContext().getMetaObject());
            }
            
        };
    }

}
