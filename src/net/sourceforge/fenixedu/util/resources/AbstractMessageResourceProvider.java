package net.sourceforge.fenixedu.util.resources;

import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

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

    protected String format(final String value, final String[] args) {
	if (StringUtils.isEmpty(value)) {
	    return value;
	}

	if (args == null || args.length == 0) {
	    return value;
	}

	return MessageFormat.format(value, (Object[]) args);

    }

}
