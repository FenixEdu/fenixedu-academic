package net.sourceforge.fenixedu.dataTransferObject.teacherServiceDistribution;

import net.sourceforge.fenixedu.domain.Person;

public class PersonPermissionsDTOEntry {
	Person person;
	Boolean phaseManagementPermission = false;
	Boolean automaticValuationPermission = false;
	Boolean omissionConfigurationPermission = false;
	Boolean tsdCoursesAndTeachersManagementPermission = false;
	Boolean coursesValuationPermission = false;
	Boolean teachersValuationPermission = false;
	Boolean teachersManagementPermission = false;
	Boolean coursesManagementPermission = false;
	
	
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

	public void setPerson(Person person) {
		this.person = person;
	}

	public Boolean getCoursesValuationPermission() {
	    return coursesValuationPermission;
	}

	public void setCoursesValuationPermission(Boolean coursesValuationPermission) {
	    this.coursesValuationPermission = coursesValuationPermission;
	}

	public Boolean getTeachersValuationPermission() {
	    return teachersValuationPermission;
	}

	public void setTeachersValuationPermission(Boolean teachersValuationPermission) {
	    this.teachersValuationPermission = teachersValuationPermission;
	}

	public Boolean getTeachersManagementPermission() {
	    return teachersManagementPermission;
	}

	public void setTeachersManagementPermission(Boolean teachersManagementPermission) {
	    this.teachersManagementPermission = teachersManagementPermission;
	}

	public Boolean getCoursesManagementPermission() {
	    return coursesManagementPermission;
	}

	public void setCoursesManagementPermission(Boolean coursesManagementPermission) {
	    this.coursesManagementPermission = coursesManagementPermission;
	}

	public Boolean getCompetenceCoursesAndTeachersManagementPermission() {
		return tsdCoursesAndTeachersManagementPermission;
	}

	public void setCompetenceCoursesAndTeachersManagementPermission(
			Boolean tsdCoursesAndTeachersManagementPermission) {
		this.tsdCoursesAndTeachersManagementPermission = tsdCoursesAndTeachersManagementPermission;
	}
}
