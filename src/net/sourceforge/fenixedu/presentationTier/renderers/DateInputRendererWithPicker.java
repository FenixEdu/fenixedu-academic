package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.renderers.DateInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlFormComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlImage;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlLink;
import net.sourceforge.fenixedu.renderers.components.HtmlScript;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * This renderer provides a more fashionable way of doing the input of a date
 * than the plain DateInputRenderer. The date is accepted from a text input
 * field using a certain format, but there is an auxiliary javascript calendar
 * that pops out on the click of a small image butoon The format beeing accepted
 * is shown to the right of the textfield and before the calendar trigger
 * button.
 * 
 * <p>
 * Example: <input type="text" value="01/02/3456"/> dd/MM/yyyy <input
 * type="button" value="Cal..." align="absmiddle" size="10"/>
 * 
 * @author José Pedro Pereira - Linkare TI
 */
public class DateInputRendererWithPicker extends DateInputRenderer {

    public static final String CALENDAR_PATH = "/javaScript/calendar/calendar.js";
    public static final String CALENDAR_SETUP_PATH = "/javaScript/calendar/calendar-setup.js";
    public static final String CALENDAR_LANG_PATH = "/javaScript/calendar/lang/calendar-%s.js";
    public static final String CALENDAR_ICON_PATH = "/javaScript/calendar/img.gif";
    
    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        HtmlContainer container = (HtmlContainer) super.createTextField(object, type);
        
        HtmlImage image = getCalendarImage();
        container.addChild(image);
        container.addChild(getSetupScripts());
        container.addChild(getCalendarScript(image));
        
        return container;
    }

    protected HtmlImage getCalendarImage() {
        HtmlImage imageCalendar = new HtmlImage();
        
        imageCalendar.setName(HtmlFormComponent.getNewName());
        imageCalendar.setId(imageCalendar.getName());
        
        imageCalendar.setHeight("100%");
        imageCalendar.setSource(getUrl(CALENDAR_ICON_PATH));
        imageCalendar.setTitle(RenderUtils.getResourceString("fenix.renderers.dataPicker.image.title"));
        
        return imageCalendar;
    }

    protected String getUrl(String base) {
        HtmlLink link = new HtmlLink();
        link.setModuleRelative(false);
        link.setContextRelative(true);
        link.setUrl(base);
        
        String imageUrl = link.calculateUrl();
        return imageUrl;
    }

    protected HtmlComponent getSetupScripts() {
        HtmlContainer container = new HtmlInlineContainer();

        Language language = LanguageUtils.getLanguage();
        switch (language) {
        case en:
        case pt:
        case es:
            break;
        default:
            language = LanguageUtils.getSystemLanguage();
        }

        String langPath = String.format(CALENDAR_LANG_PATH, language.name());
        
        container.addChild(new HtmlScript("text/javascript", getUrl(CALENDAR_PATH), true));
        container.addChild(new HtmlScript("text/javascript", getUrl(CALENDAR_SETUP_PATH), true));
        container.addChild(new HtmlScript("text/javascript", getUrl(langPath), true));
        
        return container;
    }
    
    protected HtmlScript getCalendarScript(HtmlImage image) {
        MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();
        
        String scriptCalendarSetup = getScriptText(image, key);
        
        HtmlScript script = new HtmlScript();
        script.setContentType("text/javascript");
        script.setScript(scriptCalendarSetup);
        script.setConditional(true);

        return script;
    }

    protected String getScriptText(HtmlImage image, MetaSlotKey key) {
        return String.format(
                "Calendar.setup({inputField: '%s', ifFormat: '%s', button: '%s'});",
                key.toString(),
                getInputFormatForCalendar(),
                image.getId()
        );
    }

    protected String getInputFormatForCalendar() {
        Locale locale = getLocale();
        SimpleDateFormat format = new SimpleDateFormat(getFormat(), locale);
        
        Calendar c = Calendar.getInstance();
        
        c.set(Calendar.YEAR, 1999);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, 24);
        
        String dateStringFormatted = format.format(c.getTime());
        dateStringFormatted = dateStringFormatted.replace("1999", "%Y");
        dateStringFormatted = dateStringFormatted.replace("99", "%y");
        dateStringFormatted = dateStringFormatted.replace("12", "%m");
        dateStringFormatted = dateStringFormatted.replace("24", "%e");
        
        return dateStringFormatted;
    }
    
}
 