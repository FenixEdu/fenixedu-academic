package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public class FormatRenderer extends OutputRenderer {

    private String format;
    
    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new FormatLayout();
    }

    private class FormatLayout extends Layout {

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            String formatedObject = RenderUtils.getFormatedProperties(getFormat(), object);
            
            return new HtmlText(formatedObject);
        }

    }
}
