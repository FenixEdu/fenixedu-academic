package net.sourceforge.fenixedu.renderers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.converters.DateConverter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.util.RequestUtils;

/**
 * This renderer provides a simple way of doing the input of a date. The date
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

    private String format;

    private String formatText;
    private String bundle;
    private boolean key;
    
    /**
     * The format in which the date should be displayed. The format can
     * have the form accepted by 
     * <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/text/SimpleDateFormat.html">SimpleDateFormat</a>
     * 
     * <p>
     * The default format is {@value DateConverter#DEFAULT_FORMAT}.
     * 
     * @property
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return this.format == null ? DateConverter.DEFAULT_FORMAT : format;
    }

    public boolean isFormatSet() {
        return this.format != null;
    }

    public String getFormatText() {
        return this.formatText;
    }

    /**
     * By default the value of <code>format</code> is used to show to the user
     * how to write the date. This property allows you to override that default.
     * This means that the value of the property will be shown instead.
     * 
     * @property
     */
    public void setFormatText(String formatText) {
        this.formatText = formatText;
    }

    public String getBundle() {
        return this.bundle;
    }

    /**
     * When the value of the <code>formatText</code> is a key this property indicates
     * the name of the bundle where the key will be looked for.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public boolean isKey() {
        return this.key;
    }

    /**
     * Indicates the the value of the <code>formatText</code> property is 
     * a key and not the text itself.
     * 
     * @property
     */
    public void setKey(boolean key) {
        this.key = key;
    }

    protected Locale getLocale() {
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
        
        dateInput.setConverter(getDateConverter(dateFormat));
        
        HtmlContainer container = new HtmlInlineContainer();
        container.addChild(dateInput);
        container.addChild(new HtmlText(getFormatLabel()));
        
        return container;
    }

    protected String getFormatLabel() {
        if (isKey()) {
            return RenderUtils.getResourceString(getBundle(), getFormatText());
        }
        else {
            if (getFormatText() != null) {
                return getFormatText();
            }
            else {
                return getFormat();
            }
        }
    }

    protected Converter getDateConverter(SimpleDateFormat dateFormat) {
        return new DateConverter(dateFormat);
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
 