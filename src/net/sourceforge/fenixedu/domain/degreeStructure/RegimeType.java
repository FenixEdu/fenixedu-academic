/*
 * Created on Dec 8, 2005
 */
package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RegimeType {

	SEMESTRIAL,

	ANUAL;

	public String getName() {
		return name();
	}

	public String getLocalizedName() {
		return getLocalizedName(Language.getLocale());
	}

	public String getLocalizedName(final Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getName());
	}

	public String getAcronym() {
		return getAcronym(Language.getLocale());
	}

	private String getAcronym(final Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getName() + ".ACRONYM");
	}
}
