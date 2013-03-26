package net.sourceforge.fenixedu.util;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class BundleUtil {

    private static final String RESOURCES_PREFIX = "resources.";
    private static final String RESOURCES_SUFFIX = "Resources";

    private static final String APPLICATION_MODULE = "Application";
    private static final String ENUMERATION_MODULE = "Enumeration";

    public static String getStringFromResourceBundle(final String bundle, final String key, String... arguments) {
        try {
            final ResourceBundle resourceBundle = getResourceBundleByName(bundle);
            return MessageFormat.format(resourceBundle.getString(key), (Object[]) arguments);
        } catch (MissingResourceException e) {
            return key;
        }
    }

    public static String getMessageFromModuleOrApplication(final String moduleName, final String key, final String... arguments) {
        try {
            return MessageFormat.format(getResourceBundleByModuleName(moduleName).getString(key), (Object[]) arguments);
        } catch (MissingResourceException e) {
            try {
                return MessageFormat.format(getResourceBundleByModuleName(APPLICATION_MODULE).getString(key),
                        (Object[]) arguments);
            } catch (MissingResourceException ex) {
                return key;
            }
        }
    }

    public static String getEnumName(final Enum<?> enumeration) {
        return getEnumName(enumeration, ENUMERATION_MODULE);
    }

    public static String getEnumName(final Enum<?> enumeration, final String moduleName) {
        String enumFullName = enumeration.getClass().getName();
        if (enumFullName.indexOf('$') > -1) {
            enumFullName = enumFullName.substring(0, enumFullName.indexOf('$'));
        }

        String enumSimpleName = enumeration.getClass().getSimpleName();
        if (enumSimpleName.isEmpty()) {
            enumSimpleName = enumFullName.substring(enumFullName.lastIndexOf('.') + 1);
        }

        enumFullName = enumFullName + "." + enumeration.name();
        enumSimpleName = enumSimpleName + "." + enumeration.name();
        try {
            return getResourceBundleByModuleName(moduleName).getString(enumFullName);
        } catch (MissingResourceException e) {
            try {
                return getResourceBundleByModuleName(moduleName).getString(enumSimpleName);
            } catch (MissingResourceException ex) {
                try {
                    return getResourceBundleByModuleName(moduleName).getString(enumeration.name());
                } catch (MissingResourceException exc) {
                    return enumFullName;
                }
            }
        }
    }

    private static ResourceBundle getResourceBundleByModuleName(String moduleName) {
        moduleName = StringUtils.capitalize(moduleName);
        try {
            return getResourceBundleByName(RESOURCES_PREFIX + moduleName + RESOURCES_SUFFIX);
        } catch (MissingResourceException ex) {
            return getResourceBundleByName(RESOURCES_PREFIX + moduleName);
        }
    }

    private static ResourceBundle getResourceBundleByName(final String bundleName) {
        return ResourceBundle.getBundle(bundleName, Language.getLocale());
    }
}
