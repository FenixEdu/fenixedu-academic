/**
 * 
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public enum EnrolmentModel {

    COMPLETE, CUSTOM;

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
