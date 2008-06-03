package net.sourceforge.fenixedu.presentationTier.renderers;

import pt.ist.fenixWebFramework.renderers.StringInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlotKey;

/**
 * This renderer provides a way of asking the user for a color using a color picker.
 *  
 * @author cfgi
 */
public class ColorInputRenderer extends StringInputRenderer {

	private static final String BASE_PATH = "/javaScript/picker";
	
    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        HtmlTextInput input = (HtmlTextInput) super.createTextField(object, type);
        input.setId(getContext().getMetaObject().getKey().toString());
        
        HtmlLink link = new HtmlLink();
        link.setModuleRelative(false);
        link.setUrl(BASE_PATH + "/img/");
        
        HtmlScript script = new HtmlScript();
        script.setContentType("text/javascript");
        script.setScript(
    		String.format("new Control.ColorPicker('%s', { IMAGE_BASE : '%s' });", 
    				input.getId(),
    				link.calculateUrl()));
        
        HtmlContainer container = new HtmlInlineContainer();

        container.addChild(input);
        container.addChild(script);
        
        return container;
    }
    
    @Override
    protected Layout getLayout(Object object, Class type) {
        return new ColorPickerLayout();
    }

    class ColorPickerLayout extends TextFieldLayout {

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
