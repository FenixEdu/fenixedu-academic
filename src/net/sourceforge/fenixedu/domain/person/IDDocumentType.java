/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum IDDocumentType implements IPresentableEnum {

	IDENTITY_CARD,

	PASSPORT,

	FOREIGNER_IDENTITY_CARD,

	NATIVE_COUNTRY_IDENTITY_CARD,

	NAVY_IDENTITY_CARD,

	AIR_FORCE_IDENTITY_CARD,

	OTHER,

	MILITARY_IDENTITY_CARD,

	EXTERNAL,

	CITIZEN_CARD,

	RESIDENCE_AUTHORIZATION;

	public String getName() {
		return name();
	}

	@Override
	public String getLocalizedName() {
		return getLocalizedName(Language.getLocale());
	}

	public String getLocalizedName(final Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(name());
	}
}
