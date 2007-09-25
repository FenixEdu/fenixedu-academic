package net.sourceforge.fenixedu.domain.curriculum;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.util.LanguageUtils;

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
				LanguageUtils.getLocale()).getString(getQualifiedName());
	}

}
