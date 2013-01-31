package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public enum TypeOfProgramme {
	PROJECT, THESIS, LAB, SEMINAR, COURSES;

	public String getName() {
		return name();
	}

	public String getQualifiedName() {
		return AcademicServiceRequestType.class.getSimpleName() + "." + name();
	}

	public String getFullyQualifiedName() {
		return AcademicServiceRequestType.class.getName() + "." + name();
	}

	public String getDescription() {
		ResourceBundle ENUMERATION_RESOURCES = ResourceBundle.getBundle("resources/EnumerationResources", Locale.getDefault());
		return ENUMERATION_RESOURCES.getString(getFullyQualifiedName());
	}
}
