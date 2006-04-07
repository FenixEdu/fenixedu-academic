package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.apache.log4j.Logger;

/**
 * This renderer provides a standard way of presenting a {@link MultiLanguageString}. The
 * <tt>MultiLanguageString</tt> is presented as a simple string. The string to be presented
 * is determined by the logic in {@link MultiLanguageString#getContent()}. Additionally you
 * can override the language in which the content is to be displayed with the 
 * {@link #setLanguage(String) language} property. In this case the content to be presented
 * will be determined by {@link MultiLanguageString#getContent(Language)}
 * 
 * @author cfgi
 * @author cgmp
 */
public class MultiLanguageStringRenderer extends StringRenderer {

    private static final Logger logger = Logger.getLogger(MultiLanguageStringRenderer.class);
    
    private String language; 
    
    public String getLanguage() {
        return language;
    }

    /**
     * Allows you to override the language in wich the <tt>MultiLanguageString</tt> content
     * will be presented.
     * 
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
        String value;
        
        if (getLanguage() != null) {
            try {
                Language language = Language.valueOf(getLanguage());
                value = mlString.getContent(language);
            }
            catch (IllegalArgumentException e) {
                logger.warn("specified language '" +  getLanguage() + "' is not defined, ignoring given language");
                value = mlString.getContent();
            }
        }
        else {
            value = mlString.getContent();
        }
        
        HtmlComponent component = super.renderComponent(layout, value, type);
        
        if (mlString.isRequestedLanguage()) {
            return component;
        }

        if (mlString.getAllLanguages().isEmpty()) {
            return component;
        }

        HtmlInlineContainer container = new HtmlInlineContainer();

        HtmlComponent languageComponent = renderValue(mlString.getContentLanguage(), null, null);
        
        container.addChild(component);
        container.addChild(new HtmlText(" (<strong>"));
        container.addChild(languageComponent);
        container.addChild(new HtmlText("</strong>)"));
        
        return container;
    }

}
