package net.sourceforge.fenixedu.util.resources;

import java.util.Properties;
import java.util.ResourceBundle;

public class DefaultResourceBundleProvider extends AbstractMessageResourceProvider {

    public DefaultResourceBundleProvider() {
        super();
    }

    public DefaultResourceBundleProvider(Properties bundleMappings) {
        super(bundleMappings);
    }

    public String getMessage(String key, String bundle) {
        if (containsMapping(bundle)) {
            return ResourceBundle.getBundle(getBundleMapping(bundle)).getString(key);
        } else {
            return ResourceBundle.getBundle(bundle).getString(key);
        }
    }

}
