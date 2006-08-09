package net.sourceforge.fenixedu.presentationTier.renderers.htmlEditor;

import net.sourceforge.fenixedu.presentationTier.renderers.components.TinyMceEditor;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

/**
 * Allows you to use the <a href="http://tinymce.moxiecode.com/">TinyMCE</a>
 * Javascript HTML WYSIWYG editor.
 * 
 * @author cfgi
 */
public class RichTextInputRenderer extends InputRenderer {

    private String config;
    
    private Integer width;
    private Integer height;
    
    private Integer columns;
    private Integer rows;
    
    private boolean safe;
    
    public RichTextInputRenderer() {
        super();
        
        setColumns(50);
        setRows(10);
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

    public Integer getColumns() {
        return this.columns;
    }

    /**
     * The number of column of the textarea.
     * 
     * @property
     */
    public void setColumns(Integer columns) {
        this.columns = columns;
    }

    public Integer getRows() {
        return this.rows;
    }

    /**
     * The number of rows of the textarea.
     * 
     * @property
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getHeight() {
        return this.height;
    }

    /**
     * Chooses the height in pixels of the rich text editor.
     * 
     * @property
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return this.width;
    }

    /**
     * Chooses the width in pixels of the rich text editor.
     * 
     * @property
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    public boolean isSafe() {
        return this.safe;
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

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                MetaSlotKey key = (MetaSlotKey) getInputContext().getMetaObject().getKey();

                TinyMceEditor editor = new TinyMceEditor(getConfig());
                editor.setValue((String) object);
                editor.setRows(getRows());
                editor.setColumns(getColumns());
                editor.setHeight(getHeight());
                editor.setWidth(getWidth());
                editor.setTargetSlot(key);
                
                editor.setName(key.toString());
                editor.setId(editor.getName());
                
                if (isSafe()) {
                    editor.setConverter(new SafeHtmlConverter());
                }
             
                return editor;
            }
            
        };
    }

}
