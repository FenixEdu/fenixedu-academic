package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.LabelFormatter;

public class LabelFormatterRenderer extends OutputRenderer {

    private Map<String, String> bundleNameMapping;

    public LabelFormatterRenderer() {
        super();
        this.bundleNameMapping = new HashMap<String, String>();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                final LabelFormatter labelFormatter = (LabelFormatter) object;
                final StringBuilder result = new StringBuilder();

                for (final LabelFormatter.Label label : labelFormatter.getLabels()) {
                    if (label.isUseBundle()) {
                        if (containsBundle(label.getBundle())) {
                            result.append(RenderUtils.getResourceString(getBundle(label.getBundle()),
                                    label.getKey()));
                        } else {
                            result.append(RenderUtils.getResourceString(label.getBundle(), label
                                    .getKey()));
                        }
                    } else {
                        result.append(label.getKey());
                    }
                }

                return new HtmlText(result.toString());
            }

        };
    }

    /**
     * 
     * 
     * @property
     */
    public void setBundleName(String bundle, String name) {
        this.bundleNameMapping.put(bundle, name);
    }

    public String getBundleName(String bundle) {
        return this.bundleNameMapping.get(bundle);
    }

    private boolean containsBundle(String bundle) {
        return this.bundleNameMapping.containsKey(bundle);
    }

    private String getBundle(String bundle) {
        return this.bundleNameMapping.get(bundle);
    }

}
