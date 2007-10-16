package net.sourceforge.fenixedu.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class BundleUtil {

    public static final String APPLICATION_RESOURCES = "resources.ApplicationResources";

    public static String getString(final String bundle, final Locale locale, final String key, final String... args) {
	return MessageFormat.format(ResourceBundle.getBundle(bundle, locale).getString(key), args);
    }

    public static String getString(final String key, final String... args) {
	return getString(APPLICATION_RESOURCES, LanguageUtils.getLocale(), key, args);
    }

}
