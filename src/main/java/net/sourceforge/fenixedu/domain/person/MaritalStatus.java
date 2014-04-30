/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.util.BundleUtil;
import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import java.util.Locale;

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
        final ResourceBundle bundle = ResourceBundle.getBundle("resources.EnumerationResources", I18N.getLocale());
        return bundle.getString(this.getClass().getName() + "." + name());
    }
}
