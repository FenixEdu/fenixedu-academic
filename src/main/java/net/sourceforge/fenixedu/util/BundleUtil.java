/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;

public class BundleUtil {

    private static final String RESOURCES_PREFIX = "resources.";
    private static final String RESOURCES_SUFFIX = "Resources";

    private static final String APPLICATION_MODULE = "Application";
    private static final String ENUMERATION_MODULE = "Enumeration";

    @Deprecated // remove on move to major version 4.0.0
    public static final String ENUMERATION_BUNDLE = Bundle.ENUMERATION;
    @Deprecated // remove on move to major version 4.0.0
    public static final String APPLICATION_BUNDLE = Bundle.APPLICATION;

    @Deprecated // remove on move to major version 4.0.0
    public static String getStringFromResourceBundle(final String bundle, final String key, String... args) {
        return org.fenixedu.bennu.core.i18n.BundleUtil.getString(bundle, key, args);
    }

    @Deprecated // remove on move to major version 4.0.0
    public static String getStringFromResourceBundle(final String bundle, final Locale locale, final String key, String... args) {
        return org.fenixedu.bennu.core.i18n.BundleUtil.getString(bundle, locale, key, args);
    }

    @Deprecated // remove on move to major version 4.0.0
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

    @Deprecated // remove on move to major version 4.0.0
    public static String getEnumName(final Enum<?> enumeration) {
        return getEnumName(enumeration, ENUMERATION_MODULE);
    }

    @Deprecated // remove on move to major version 4.0.0
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
        return ResourceBundle.getBundle(bundleName, I18N.getLocale());
    }

}
