package net.sourceforge.fenixedu.renderers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

import org.apache.struts.util.RequestUtils;

public class DateRenderer extends OutputRenderer {

    private String format;
    
    public String getFormat() {
        return format == null ? "dd/MM/yyyy" : format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Date date = (Date) object;
                
                if (date == null) {
                    return new HtmlText();
                }
                
                HttpServletRequest request = getOutputContext().getViewState().getRequest();
                Locale locale = RequestUtils.getUserLocale(request, null);
                DateFormat dateFormat = new SimpleDateFormat(getFormat(), locale);
                
                return new HtmlText(dateFormat.format(date));
            }
            
        };
    }
}
