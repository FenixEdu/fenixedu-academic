package net.sourceforge.fenixedu.renderers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

import org.apache.struts.util.RequestUtils;

/**
 * This renderer provides a default input for a date. The date
 * is accepted from a text input field using a certain format. The 
 * format beeing accepted is shown to the right.
 * 
 * <p>
 * Example:
 *  <input type="text" value="01/02/3456"/> dd/MM/yyyy
 * 
 * @author cfgi
 */
public class DateInputRenderer extends TextFieldRenderer {

    private static final String DEFAULT_FORMAT = "dd/MM/yyyy";
    
    private String format;
    
    public String getFormat() {
        return format == null ? DEFAULT_FORMAT : format;
    }

    /**
     * The format in which the date should be displayed. The format can
     * have the form accepted by 
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * 
     * <p>
     * The default format is {@value #DEFAULT_FORMAT}.
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    private Locale getLocale() {
        HttpServletRequest request = getInputContext().getViewState().getRequest();
        return RequestUtils.getUserLocale(request, null);
    }

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        Date date = (Date) object;
        
        Locale locale = getLocale();
        SimpleDateFormat dateFormat = new SimpleDateFormat(getFormat(), locale);
        
        HtmlTextInput dateInput = new HtmlTextInput();
        
        if (date != null) {
            dateInput.setValue(dateFormat.format(date));
        }
        
        dateInput.setConverter(new DateConverter(dateFormat));
        
        HtmlContainer container = new HtmlInlineContainer();
        container.addChild(dateInput);
        container.addChild(new HtmlText(getFormat()));
        
        return container;
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        return new DateTextFieldLayout();
    }

    class DateTextFieldLayout extends TextFieldLayout {

        @Override
        protected void setContextSlot(HtmlComponent component, MetaSlotKey slotKey) {
            HtmlContainer container = (HtmlContainer) component;
            
            super.setContextSlot(container.getChildren().get(0), slotKey);
        }

        @Override
        public void applyStyle(HtmlComponent component) {
            HtmlContainer container = (HtmlContainer) component;
            
            super.applyStyle(container.getChildren().get(0));
        }
        
    }
}

class DateConverter extends Converter {
    private SimpleDateFormat format;
    
    public DateConverter(SimpleDateFormat format) {
        this.format = format;
    }

    @Override
    public Object convert(Class type, Object value) {
        try {
            return format.parse((String) value);
        } catch (ParseException e) {
            throw new ConversionException("could not convert input \"" + value + "\" to a date", e);
        }
    }
}
