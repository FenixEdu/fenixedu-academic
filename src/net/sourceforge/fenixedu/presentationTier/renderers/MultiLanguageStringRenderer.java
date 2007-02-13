package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.renderers.StringRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlContainer;
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
    private boolean languageShown;
    private boolean inline;
    private String languageClasses;
    
    public MultiLanguageStringRenderer() {
        super();
        
        setLanguageShown(true);
        setInline(true);
    }

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

    public boolean isInline() {
        return this.inline;
    }

    /**
     * Allows you to choose if a span or a div will be generated around the multi language string. 
     * This can be usefull if the multi-language string contains much information or html code. 
     * 
     * @property
     */
    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isLanguageShown() {
        return this.languageShown;
    }

    /**
     * Whenever a multi-language string is shown in a language that is not what the user requested
     * an annotation is added to shown in wich language the text is in. This property allows you
     * to override that behaviour.
     * 
     * @property
     */
    public void setLanguageShown(boolean languageShown) {
        this.languageShown = languageShown;
    }

    public String getLanguageClasses() {
        return this.languageClasses;
    }

    /**
     * Choose the css class to apply to the annotation showing the value's
     * language when it isn't in the requested language.
     * 
     * @property
     */
    public void setLanguageClasses(String languageClasses) {
        this.languageClasses = languageClasses;
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
        component.setLanguage(mlString.getContentLanguage().toString());

        if (mlString.isRequestedLanguage()) {
            return component;
        }

        if (mlString.getAllLanguages().isEmpty()) {
            return component;
        }

        if (! isLanguageShown()) {
            return component;
        }

        HtmlContainer container = isInline() ? new HtmlInlineContainer() : new HtmlBlockContainer();
        container.addChild(component);
        container.setIndented(false);

        HtmlComponent languageComponent = renderValue(mlString.getContentLanguage(), null, null);
        languageComponent.setClasses(getLanguageClasses());
        
        container.addChild(new HtmlText(" (", false));
        container.addChild(languageComponent);
        container.addChild(new HtmlText(")", false));
        
        return container;
    }

}
