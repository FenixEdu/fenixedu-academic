package net.sourceforge.fenixedu.presentationTier.renderers;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.presentationTier.renderers.components.HtmlEditor;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlInlineContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlSubmitButton;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaSlotKey;

public class HtmlEditorRenderer extends InputRenderer {

    private int columns;
    private int rows;
    private int heigth;
    private int width;
    
    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getHeigth() {
        return this.heigth;
    }

    public void setHeigth(int heigth) {
        this.heigth = heigth;
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new EditorLayout();
    }

    private class EditorLayout extends Layout {

        private boolean isBrowserSupported() {
            String[] notSupported = { "safari", "konqueror", "opera" }; 
            
            HttpServletRequest request = getInputContext().getViewState().getRequest();
            
            String userAgent = request.getHeader("User-Agent");
            if (userAgent == null) {
                return false;
            }
            
            userAgent = userAgent.toLowerCase();
            for (int i = 0; i < notSupported.length; i++) {
                String id = notSupported[i];
                
                if (userAgent.indexOf(id) != -1) {
                    return false;
                }
            }
            
            return true;
        }
        
        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            // TODO: cfgi, rich text editor also verifies browser so is this needed?
            if (! isBrowserSupported()) {
                return createTextArea(object, type);
            }
            else {
                return createEditor(object, type);
            }
        }

        private HtmlComponent createEditor(Object object, Class type) {
            HtmlInlineContainer container = new HtmlInlineContainer();

            HtmlEditor editor = new HtmlEditor();
            
            editor.setWidth(getWidth());
            editor.setHeigth(getHeigth());
            
            editor.setValue((String) object);
            editor.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            
            HtmlSubmitButton submitButton = getInputContext().getForm().getSubmitButton();
            
            String currentScript = submitButton.getOnClick();
            submitButton.setOnClick("updateRTE('" + editor.getName() + "');" + (currentScript == null ? "" : currentScript));
            
            container.addChild(editor);
            return container;
        }

        private HtmlComponent createTextArea(Object object, Class type) {
            HtmlTextArea textArea = new HtmlTextArea();
            textArea.setTargetSlot((MetaSlotKey) getInputContext().getMetaObject().getKey());
            
            textArea.setValue((String) object);
            
            textArea.setColumns(getColumns());
            textArea.setRows(getRows());
            
            return textArea;
        }
    }
}
