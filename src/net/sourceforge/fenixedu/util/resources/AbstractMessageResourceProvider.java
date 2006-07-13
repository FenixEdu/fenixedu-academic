package net.sourceforge.fenixedu.util.resources;

import java.util.Properties;

public abstract class AbstractMessageResourceProvider implements IMessageResourceProvider {

    private Properties bundleMapping;

    public AbstractMessageResourceProvider() {
        this.bundleMapping = new Properties();
    }

    public AbstractMessageResourceProvider(Properties mappings) {
        this.bundleMapping = mappings;
    }

    public void addMapping(String bundleName, String bundleMapping) {
        this.bundleMapping.put(bundleName, bundleMapping);
    }

    public String getBundleMapping(String bundleName) {
        return this.bundleMapping.getProperty(bundleName);
    }

    public boolean containsMapping(String bundleName) {
        return this.bundleMapping.containsKey(bundleName);
    }

}
