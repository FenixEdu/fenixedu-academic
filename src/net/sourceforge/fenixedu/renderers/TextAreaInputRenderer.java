package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

/**
 * This renderer provides an input mechanism for a string that allows long
 * texts to be inserted or span over multiple lines.
 * 
 * <p>
 * Example:
 * <textarea>&lt;the text&gt;</textarea>
 * 
 * @author cfgi
 */
public class TextAreaInputRenderer extends InputRenderer {

    private Integer columns;
    private Integer rows;
    
    public Integer getColumns() {
        return this.columns;
    }

    /**
     * The number of columns of the generated text area.
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
     * The number of rows of the generated text area.
     * 
     * @property
     */
    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                HtmlTextArea textArea = new HtmlTextArea();
                textArea.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
                
                textArea.setValue((String) object);
                
                
                return textArea;
            }

            @Override
            public void applyStyle(HtmlComponent component) {
                super.applyStyle(component);
                
                HtmlTextArea textArea = (HtmlTextArea) component; 
                
                textArea.setColumns(getColumns());
                textArea.setRows(getRows());
            }
        };
    }

}
