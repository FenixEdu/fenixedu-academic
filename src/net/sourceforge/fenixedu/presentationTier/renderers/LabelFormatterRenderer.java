package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.Properties;

import net.sourceforge.fenixedu.presentationTier.renderers.util.RendererMessageResourceProvider;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class LabelFormatterRenderer extends OutputRenderer {

    private Properties bundleMappings;

    public LabelFormatterRenderer() {
        super();

        this.bundleMappings = new Properties();

    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                return new HtmlText(((LabelFormatter) object)
                        .toString(new RendererMessageResourceProvider(
                                LabelFormatterRenderer.this.bundleMappings)));
            }

        };
    }

    /**
     * 
     * 
     * @property
     */
    public void setBundleName(String bundle, String name) {
        this.bundleMappings.put(bundle, name);
    }

    public String getBundleName(String bundle) {
        return this.bundleMappings.getProperty(bundle);
    }

}
