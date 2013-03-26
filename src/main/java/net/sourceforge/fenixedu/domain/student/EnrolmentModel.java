/**
 * 
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
        return StringAppender.append(this.getClass().getSimpleName(), ".", name());
    }

    protected String localizedName() {
        return localizedName(Language.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
