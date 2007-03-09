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

/**
 * The renderer allows you to present dates in a simple way.
 * 
 * @author cfgi
 */
public class DateRenderer extends OutputRenderer {
    
    private static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    private String format;
    
    /**
     * The format in which the date should be displayed. The format can
     * have the form accepted by {@link SimpleDateFormat}.
     * 
     * <p>
     * The default format is {@value #DEFAULT_FORMAT}.
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format == null ? DEFAULT_FORMAT : format;
    }

    public boolean isFormatSet() {
        return this.format != null;
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
                
                HttpServletRequest request = getContext().getViewState().getRequest();
                Locale locale = RequestUtils.getUserLocale(request, null);
                DateFormat dateFormat = new SimpleDateFormat(getFormat(), locale);
                
                return new HtmlText(dateFormat.format(date));
            }
            
        };
    }
}
