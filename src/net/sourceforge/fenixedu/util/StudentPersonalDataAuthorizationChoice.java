/*
 * Created on 28/Ago/2005
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

/**
 * @author Ricardo Rodrigues
 * 
 */
public enum StudentPersonalDataAuthorizationChoice {

	PROFESSIONAL_ENDS(false), /*
								* only to professional ends (job propositions, scholarships, internship, etc)
								*/
	SEVERAL_ENDS(false), /*
							* several non comercial ends (biographic, recreational, cultural, etc)
							*/
	ALL_ENDS(false), /* all ends, including comercial ones */

	NO_END(false), /* doesn't authorize the use of the data */

	STUDENTS_ASSOCIATION(true); /* allows for the student association */

	private boolean forStudentsAssociation;

	private StudentPersonalDataAuthorizationChoice(boolean forStudentsAssociation) {
		setForStudentsAssociation(forStudentsAssociation);
	}

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return StudentPersonalDataAuthorizationChoice.class.getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return StudentPersonalDataAuthorizationChoice.class.getName() + "." + name();
	}

	public String getDescription() {
		final ResourceBundle ENUMERATION_RESOURCES =
				ResourceBundle.getBundle("resources.EnumerationResources", Language.getLocale());
		return ENUMERATION_RESOURCES.getString(getQualifiedName());
	}

	public static List<StudentPersonalDataAuthorizationChoice> getGeneralPersonalDataAuthorizationsTypes() {
		List<StudentPersonalDataAuthorizationChoice> authorizationsTypes =
				new ArrayList<StudentPersonalDataAuthorizationChoice>();
		StudentPersonalDataAuthorizationChoice[] values = StudentPersonalDataAuthorizationChoice.values();
		for (StudentPersonalDataAuthorizationChoice studentPersonalDataAuthorizationChoice : values) {
			if (!studentPersonalDataAuthorizationChoice.isForStudentsAssociation()) {
				authorizationsTypes.add(studentPersonalDataAuthorizationChoice);
			}
		}
		return authorizationsTypes;
	}

	public static StudentPersonalDataAuthorizationChoice getPersonalDataAuthorizationForStudentsAssociationType(
			boolean allowsForStudentAssociation) {
		if (allowsForStudentAssociation) {
			return STUDENTS_ASSOCIATION;
		} else {
			return NO_END;
		}
	}

	public void setForStudentsAssociation(boolean forStudentsAssociation) {
		this.forStudentsAssociation = forStudentsAssociation;
	}

	public boolean isForStudentsAssociation() {
		return forStudentsAssociation;
	}
}