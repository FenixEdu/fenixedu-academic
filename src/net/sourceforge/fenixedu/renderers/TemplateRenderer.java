package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTemplate;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class TemplateRenderer extends OutputRenderer {

    private String template;
    
	public String getTemplate() {
        return template;
    }

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
