/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

public enum RegimeType {

    SEMESTRIAL,

    ANUAL;

    public String getName() {
        return name();
    }

    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getName());
    }

    public String getAcronym() {
        return getAcronym(I18N.getLocale());
    }

    private String getAcronym(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getName() + ".ACRONYM");
    }
}
