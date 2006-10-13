package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.presentationTier.renderers.components.TinyMceEditor;
import net.sourceforge.fenixedu.renderers.components.HtmlSimpleValueComponent;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;

/**
 * This renderer has the same behaviour than {@link net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageTextInputRenderer}
 * but uses a rich-text editor in place of a textarea for browsers that support it.
 * 
 * @author cfgi
 */
public class MultiLanguageRichTextInputRenderer extends MultiLanguageTextInputRenderer {

    private String config;
    private boolean safe;
    
    public boolean isSafe() {
        return safe;
    }

    /**
     * If this property is set to <tt>true</tt> then the input will be filtered and any
     * unsupported HTML will be removed or escaped to the corresponding entities. 
     * 
     * @property
     */
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

    public String getConfig() {
        return this.config;
    }

    /**
     * Allows you to choose a configuration for the editor. The given name
     * is used to fetch the properties from a file located in {@value TinyMceEditor#CONFIG_PATH} with
     * the same name and ending in ".properties".
     * 
     * @property
     */
    public void setConfig(String config) {
        this.config = config;
    }

    @Override
    protected HtmlSimpleValueComponent getInputComponent() {
        TinyMceEditor editor = new TinyMceEditor();
        
        editor.setConfig(getConfig());
        editor.setColumns(getColumns());
        editor.setRows(getRows());
        
        return editor;
    }

    @Override
    protected void configureInputComponent(HtmlSimpleValueComponent textInput) {
        super.configureInputComponent(textInput);
        
        textInput.setId(textInput.getName());
    }

    @Override
    protected Converter getConverter() {
        if (isSafe()) {
            return new MultiLanguageStringSafeHtmlConverter();
        }
        else {
            return super.getConverter();
        }
    }
    
}
