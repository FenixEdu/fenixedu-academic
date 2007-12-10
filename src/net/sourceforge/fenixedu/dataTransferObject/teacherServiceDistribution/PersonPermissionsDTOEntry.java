package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.Person;

public class PersonPermissionsDTOEntry {
	Person person;
	Boolean phaseManagementPermission = false;
	Boolean automaticValuationPermission = false;
	Boolean omissionConfigurationPermission = false;
	Boolean tsdCoursesAndTeachersManagementPermission = false;
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

	public Boolean getCompetenceCoursesAndTeachersManagementPermission() {
		return tsdCoursesAndTeachersManagementPermission;
	}

	public void setCompetenceCoursesAndTeachersManagementPermission(
			Boolean tsdCoursesAndTeachersManagementPermission) {
		this.tsdCoursesAndTeachersManagementPermission = tsdCoursesAndTeachersManagementPermission;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
}
