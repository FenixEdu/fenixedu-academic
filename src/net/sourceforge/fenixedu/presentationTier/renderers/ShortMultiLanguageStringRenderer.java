package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.MultiLanguageString;

/**
 * This renderer has the same behaviour as the {@link net.sourceforge.fenixedu.presentationTier.renderers.MultiLanguageStringRenderer} but the context is
 * limited to a certain number of characters.
 * 
 * @author cfgi
 */
public class ShortMultiLanguageStringRenderer extends MultiLanguageStringRenderer {

    private Integer length;

    public Integer getLength() {
        return this.length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    protected String getRenderedText(MultiLanguageString mlString) {
        String content = super.getRenderedText(mlString);

        if (content != null && getLength() != null) {
            if (content.length() > getLength()) {
                return content.substring(0, getLength()) + "...";
            }
        }
        
        return content;
    }

    @Override
    protected HtmlComponent renderComponent(Layout layout, Object object, Class type) {
        HtmlComponent component = super.renderComponent(layout, object, type);
        
        MultiLanguageString mlString = (MultiLanguageString) object;
        
        String previous = super.getRenderedText(mlString);
        String current = getRenderedText(mlString);
        
        if (! String.valueOf(previous).equals(String.valueOf(current))) {
            component.setTitle(previous);
        }

        return component;
    }
}
