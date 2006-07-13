package net.sourceforge.fenixedu.presentationTier.renderers.util;

import java.util.Properties;

import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.util.resources.AbstractMessageResourceProvider;

public class RendererMessageResourceProvider extends AbstractMessageResourceProvider {

    public RendererMessageResourceProvider() {
        super();
    }

    public RendererMessageResourceProvider(Properties bundleMappings) {
        super(bundleMappings);
    }

    public String getMessage(String key, String bundle) {
        if (containsMapping(bundle)) {
            return RenderUtils.getResourceString(getBundleMapping(bundle), key);
        } else {
            return RenderUtils.getResourceString(bundle, key);
        }
    }

}
