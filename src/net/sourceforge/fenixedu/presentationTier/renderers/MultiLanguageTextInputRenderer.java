package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.TextAreaInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextArea;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class MultiLanguageTextInputRenderer extends TextAreaInputRenderer {

    @Override
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        MultiLanguageString mls = (MultiLanguageString) object;
        
        HtmlTextArea textArea = (HtmlTextArea) super.renderComponent(layout, mls.getContent(), type);
        textArea.setConverter(new MultiLanguageStringInputRenderer.MultiLanguageStringConverter());
        
        return textArea;
    }
    
}
