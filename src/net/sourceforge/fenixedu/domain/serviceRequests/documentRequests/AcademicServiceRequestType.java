package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.ArrayList;
import java.util.List;

public enum AcademicServiceRequestType {

    DOCUMENT,

    REINGRESSION(true),

    EQUIVALENCE_PLAN(true),

    REVISION_EQUIVALENCE_PLAN(true),

    COURSE_GROUP_CHANGE_REQUEST(true),

    FREE_SOLICITATION_ACADEMIC_REQUEST(true),

    EXTRA_EXAM_REQUEST(true),

    PHOTOCOPY_REQUEST(true),

    PARTIAL_REGIME_REQUEST(true);

    private boolean isServiceRequest;

    AcademicServiceRequestType() {
	this.isServiceRequest = false;
    }

    AcademicServiceRequestType(final boolean isForServiceRequest) {
	this.isServiceRequest = isForServiceRequest;
    }

    public String getName() {
	return name();
    }

    public String getQualifiedName() {
	return AcademicServiceRequestType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
	return AcademicServiceRequestType.class.getName() + "." + name();
    }

    static public List<AcademicServiceRequestType> getServiceRequests() {
	final List<AcademicServiceRequestType> result = new ArrayList<AcademicServiceRequestType>();
	for (final AcademicServiceRequestType type : values()) {
	    if (type.isServiceRequest) {
		result.add(type);
	    }
	}
	return result;
    }
}
