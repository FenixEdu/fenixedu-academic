package net.sourceforge.fenixedu.util.resources;

import java.util.Properties;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class DefaultResourceBundleProvider extends AbstractMessageResourceProvider {

    public DefaultResourceBundleProvider() {
	super();
    }

    public DefaultResourceBundleProvider(Properties bundleMappings) {
	super(bundleMappings);
    }

    public String getMessage(String bundle, String key, String... args) {
	if (containsMapping(bundle)) {
	    return format(ResourceBundle.getBundle(getBundleMapping(bundle), Language.getLocale()).getString(key), args);
	} else {
	    return format(ResourceBundle.getBundle(bundle, Language.getLocale()).getString(key), args);
	}
    }

}
