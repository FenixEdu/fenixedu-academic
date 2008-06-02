package net.sourceforge.fenixedu.domain.curriculum;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Luis Cruz
 * 
 */
public enum EnrollmentState {

	APROVED,

	NOT_APROVED,

	ENROLLED,

	TEMPORARILY_ENROLLED,

	ANNULED,

	NOT_EVALUATED;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return EnrollmentState.class.getSimpleName() + "." + name();
	}

	public String getDescription() {
		return ResourceBundle.getBundle("resources.EnumerationResources",
				Language.getLocale()).getString(getQualifiedName());
	}

}
