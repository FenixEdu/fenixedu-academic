/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.util;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum RegimeType {

    INTEGRAL_TIME, PARTIAL_TIME, EXCLUSIVENESS;

    public String getName() {
        return name();
    }

    public String localizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(qualifiedName());
    }

    protected String qualifiedName() {
        return this.getClass().getSimpleName() + "." + name();
    }

    protected String localizedName() {
        return localizedName(I18N.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
