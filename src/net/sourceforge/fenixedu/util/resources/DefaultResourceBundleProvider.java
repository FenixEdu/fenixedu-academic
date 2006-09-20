package net.sourceforge.fenixedu.util.resources;

import java.util.Properties;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

public class DefaultResourceBundleProvider extends AbstractMessageResourceProvider {

    public DefaultResourceBundleProvider() {
        super();
    }

    public DefaultResourceBundleProvider(Properties bundleMappings) {
        super(bundleMappings);
    }

    public String getMessage(String key, String bundle) {
        if (containsMapping(bundle)) {
            return ResourceBundle.getBundle(getBundleMapping(bundle), LanguageUtils.getLocale()).getString(key);
        } else {
            return ResourceBundle.getBundle(bundle, LanguageUtils.getLocale()).getString(key);
        }
    }

}
