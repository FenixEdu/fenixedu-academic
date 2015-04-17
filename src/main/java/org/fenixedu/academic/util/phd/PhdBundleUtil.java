/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.util.phd;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.commons.i18n.I18N;

/**
 * Remove usages of this class since it shamefully abuses on bundle key dark magic
 */
@Deprecated
public class PhdBundleUtil {
    private static final String RESOURCES_PREFIX = "resources.";
    private static final String RESOURCES_SUFFIX = "Resources";

    private static final String APPLICATION_MODULE = "Application";
    private static final String ENUMERATION_MODULE = "Enumeration";

    @Deprecated
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

    @Deprecated
    public static String getEnumName(final Enum<?> enumeration) {
        return getEnumName(enumeration, ENUMERATION_MODULE);
    }

    @Deprecated
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
