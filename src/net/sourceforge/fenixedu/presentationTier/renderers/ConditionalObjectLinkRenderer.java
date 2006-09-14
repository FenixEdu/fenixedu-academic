package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;

public class ConditionalObjectLinkRenderer extends ObjectLinkRenderer {

    /**
     * This render is used to create a link out of an object if a given boolean
     * property is true. You choose the link format and some properties can be
     * used to configure the link. You can also specify the link indirectly by
     * specifing a destination and then defining a destination with that name in
     * the place were ou use this renderer.
     * 
     * <p>
     * The link body is configured through a sub rendering of the object with
     * the specified layout and schema.
     * 
     * <p>
     * Example: <a href="#">Jane Doe</a>
     * 
     * @author pcma
     */

    private String visibleIf;

    public String getVisibleIf() {
        return visibleIf;
    }

    public void setVisibleIf(String visibleIf) {
        this.visibleIf = visibleIf;
    }

    protected Layout getLayout(Object object, Class type) {
        final Layout layout = super.getLayout(object, type);

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                Boolean visible = Boolean.FALSE;
                try {
                    visible = (Boolean) RendererPropertyUtils.getProperty(object, getVisibleIf(), false);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

                if (visible) {
                    return layout.createComponent(object, type);
                } else {
                    String text = getText();
                    return (text != null) ? new HtmlText(text) : renderValue(object, RenderKit
                            .getInstance().findSchema(getSubSchema()), getSubLayout());
                }
            }

        };
    }
}
