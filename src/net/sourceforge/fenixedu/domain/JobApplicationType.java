package net.sourceforge.fenixedu.domain;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum JobApplicationType {

	ANNOUNCEMENT, PUBLIC_ANNOUNCEMENT, SPONTANEOUS_PROPOSAL, EMPLOYMENT_AGENCY, DEPARTMENTS, UNIVA, AEIST, IAESTE, IEFP,
	PERSONAL_CONTACT, ENTERPRENEURSHIP, HEAD_HUNTERS, OTHERS;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return JobApplicationType.class.getSimpleName() + "." + name();
	}

	public String getLocalizedName() {
		return getLocalizedName(Language.getLocale());
	}

	public String getLocalizedName(Locale locale) {
		return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
	}

}
