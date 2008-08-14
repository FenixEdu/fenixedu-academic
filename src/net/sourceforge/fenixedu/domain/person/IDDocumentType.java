/*
 * Created on Apr 15, 2005
 */
package net.sourceforge.fenixedu.domain.person;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum IDDocumentType {

    IDENTITY_CARD,

    PASSPORT,

    FOREIGNER_IDENTITY_CARD,

    NATIVE_COUNTRY_IDENTITY_CARD,

    NAVY_IDENTITY_CARD,

    AIR_FORCE_IDENTITY_CARD,

    OTHER,

    MILITARY_IDENTITY_CARD,

    EXTERNAL;

    public String getName() {
	return name();
    }

    public String getLocalizedName() {
	return ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale()).getString(name());
    }

}
