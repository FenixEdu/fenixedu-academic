package net.sourceforge.fenixedu.presentationTier.renderers.util;

import java.util.Properties;

import pt.utl.ist.fenix.tools.resources.AbstractMessageResourceProvider;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class RendererMessageResourceProvider extends AbstractMessageResourceProvider {

    public RendererMessageResourceProvider() {
	super();
    }

    public RendererMessageResourceProvider(Properties bundleMappings) {
	super(bundleMappings);
    }

    public String getMessage(String bundle, String key, String... args) {
	if (containsMapping(bundle)) {
	    return format(RenderUtils.getResourceString(getBundleMapping(bundle), key), args);
	} else {
	    return format(RenderUtils.getResourceString(bundle, key), args);
	}
    }
}
