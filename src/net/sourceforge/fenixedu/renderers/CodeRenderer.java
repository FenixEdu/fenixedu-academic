package net.sourceforge.fenixedu.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlPreformattedText;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;

public class CodeRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                String text = (String) object;
                
                if (text == null) {
                    return new HtmlText();
                }
                
                HtmlPreformattedText container = new HtmlPreformattedText();
                container.setIndented(false);

                container.addChild(new HtmlText(text, true, true));
                
                return container;
            }
            
        };
    }

}
