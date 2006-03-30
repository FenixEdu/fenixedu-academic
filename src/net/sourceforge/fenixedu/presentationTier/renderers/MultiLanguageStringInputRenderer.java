package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.StringInputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTextInput;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class MultiLanguageStringInputRenderer extends StringInputRenderer {

    @Override
    protected HtmlComponent createTextField(Object object, Class type) {
        MultiLanguageString mls = (MultiLanguageString) object;
        
        HtmlTextInput input = (HtmlTextInput) super.createTextField(mls.getContent(), type);
        input.setConverter(new MultiLanguageStringConverter());
        
        return input;
    }

    public static class MultiLanguageStringConverter extends Converter {

        @Override
        public Object convert(Class type, Object value) {
            String text = (String) value;
            
            MultiLanguageString mlString = new MultiLanguageString();
            mlString.setContent(text);
            
            return mlString;
        }
        
    }
}
