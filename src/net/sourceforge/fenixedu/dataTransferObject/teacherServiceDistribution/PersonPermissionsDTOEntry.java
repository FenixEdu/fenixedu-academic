package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.Person;

public class PersonPermissionsDTOEntry {
	Person person;
	Boolean phaseManagementPermission = false;
	Boolean automaticValuationPermission = false;
	Boolean omissionConfigurationPermission = false;
	Boolean valuationCompetenceCoursesAndTeachersManagementPermission = false;
	Boolean coursesAndTeachersValuationPermission = false;
	Boolean coursesAndTeachersManagementPermission = false;
	
	public Boolean getCoursesAndTeachersManagementPermission() {
		return coursesAndTeachersManagementPermission;
	}

	public void setCoursesAndTeachersManagementPermission(Boolean coursesAndTeachersManagementPermission) {
		this.coursesAndTeachersManagementPermission = coursesAndTeachersManagementPermission;
	}

	public Boolean getCoursesAndTeachersValuationPermission() {
		return coursesAndTeachersValuationPermission;
	}

	public void setCoursesAndTeachersValuationPermission(Boolean coursesAndTeachersValuationPermission) {
		this.coursesAndTeachersValuationPermission = coursesAndTeachersValuationPermission;
	}

	public PersonPermissionsDTOEntry(Person person) {
		this.person = person;
	}

	public Boolean getPhaseManagementPermission() {
		return phaseManagementPermission;
	}

	public void setPhaseManagementPermission(Boolean phaseManagementPermission) {
		this.phaseManagementPermission = phaseManagementPermission;
	}

	public Person getPerson() {
		return person;
	}

	public Boolean getAutomaticValuationPermission() {
		return automaticValuationPermission;
	}

	public void setAutomaticValuationPermission(Boolean automaticValuationPermission) {
		this.automaticValuationPermission = automaticValuationPermission;
	}

	public Boolean getOmissionConfigurationPermission() {
		return omissionConfigurationPermission;
	}

	public void setOmissionConfigurationPermission(Boolean omissionConfiguration) {
		this.omissionConfigurationPermission = omissionConfiguration;
	}

	public Boolean getValuationCompetenceCoursesAndTeachersManagementPermission() {
		return valuationCompetenceCoursesAndTeachersManagementPermission;
	}

	public void setValuationCompetenceCoursesAndTeachersManagementPermission(
			Boolean valuationCompetenceCoursesAndTeachersManagementPermission) {
		this.valuationCompetenceCoursesAndTeachersManagementPermission = valuationCompetenceCoursesAndTeachersManagementPermission;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
