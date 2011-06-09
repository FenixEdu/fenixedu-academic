/*
 * Created on Dec 5, 2005
 *	by mrsp
 */
package net.sourceforge.fenixedu.util;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.StringAppender;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RegimeType {

    INTEGRAL_TIME, PARTIAL_TIME, EXCLUSIVENESS;

    public String getName() {
	return name();
    }

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
