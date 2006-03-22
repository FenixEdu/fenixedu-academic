package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class MultiLanguageStringRenderer extends StringRenderer {

    private String language; 
    
    public String getLanguage() {
        return language;
    }

    /**
     * @property 
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        if (object == null) {
            return super.renderComponent(layout, null, type);
        }
        
        MultiLanguageString mlString = (MultiLanguageString) object;
        
        String value = getValueWithLanguage(mlString, getLanguage());
        return super.renderComponent(layout, value, type);
    }

    private String getValueWithLanguage(MultiLanguageString mlString, String language) {
        String value = mlString.getContent(getUsedLanguage(language));
        
        if (value != null) {
            return value;
        }
        
        return mlString.getContent(Language.getDefaultLanguage());
    }

    private Language getUsedLanguage(String language) {
        if (language != null) {
            return Language.valueOf(language.toLowerCase());
        }
        
        HttpServletRequest request = getContext().getViewState().getRequest();
        Locale locale = request.getLocale();

        if (locale == null) {
            return Language.getDefaultLanguage();
        }
        
        return Language.valueOf(locale.getLanguage().toLowerCase());
    }
    
}
