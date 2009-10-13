package net.sourceforge.fenixedu.dataTransferObject.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

public class AcademicServiceRequestSearchBean implements Serializable {

    private AcademicServiceRequestType academicServiceRequestType;

    private AcademicServiceRequestSituationType academicServiceRequestSituationType;

    private boolean urgentRequest;

    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return academicServiceRequestType;
    }

    public void setAcademicServiceRequestType(AcademicServiceRequestType academicServiceRequestType) {
	this.academicServiceRequestType = academicServiceRequestType;
    }

    public AcademicServiceRequestSituationType getAcademicServiceRequestSituationType() {
	return academicServiceRequestSituationType;
    }

    public void setAcademicServiceRequestSituationType(AcademicServiceRequestSituationType academicServiceRequestSituationType) {
	this.academicServiceRequestSituationType = academicServiceRequestSituationType;
    }

    public boolean getUrgentRequest() {
	return urgentRequest;
    }

    public void setUrgentRequest(boolean urgentRequest) {
	this.urgentRequest = urgentRequest;
    }

    public boolean isUrgentRequest() {
	return urgentRequest;
    }
}
