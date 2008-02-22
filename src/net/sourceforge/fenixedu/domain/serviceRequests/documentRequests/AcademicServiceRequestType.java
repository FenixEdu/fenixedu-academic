package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

public enum AcademicServiceRequestType {

    DOCUMENT,

    REINGRESSION,

    EQUIVALENCE_PLAN,

    REVISION_EQUIVALENCE_PLAN,

    COURSE_GROUP_CHANGE_REQUEST,

    FREE_SOLICITATION_ACADEMIC_REQUEST,

    EXTRA_EXAM_REQUEST,

    PHOTOCOPY_REQUEST;

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return AcademicServiceRequestType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return AcademicServiceRequestType.class.getName() + "." + name();
    }

}
