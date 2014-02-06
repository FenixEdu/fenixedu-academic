/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum MaritalStatus implements IPresentableEnum {

    SINGLE,

    MARRIED,

    DIVORCED,

    WIDOWER,

    SEPARATED,

    CIVIL_UNION,

    // TODO: RAIDES Provider and beans exclude this value.
    // This enum should be refactored to contain an "isActive"
    UNKNOWN;

    public String getPresentationName() {
        return BundleUtil.getStringFromResourceBundle("resources/EnumerationResources", name());
    }

    @Override
    public String getLocalizedName() {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
        return bundle.getString(this.getClass().getName() + "." + name());
    }

    public String getLocalizedName(final Locale locale) {
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", locale);
        return bundle.getString(this.getClass().getName() + "." + name());
    }

    public static MaritalStatus parse(final String status, final Locale locale) {
        for (MaritalStatus maritialStatus : MaritalStatus.values()) {
            if (StringUtils.equalsIgnoreCase(status, maritialStatus.getLocalizedName(locale))) {
                return maritialStatus;
            }
        }
        return null;
    }
}
