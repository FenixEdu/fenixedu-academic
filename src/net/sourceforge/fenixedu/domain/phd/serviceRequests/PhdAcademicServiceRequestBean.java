package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

import org.joda.time.DateTime;

public class PhdAcademicServiceRequestBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private PhdAcademicServiceRequest academicServiceRequest;
    private AcademicServiceRequestSituationType situationType;
    private DateTime whenNewSituationOccured = new DateTime();
    private String justification;

    public PhdAcademicServiceRequestBean(final PhdAcademicServiceRequest request) {
	this.academicServiceRequest = request;
    }

    public PhdAcademicServiceRequest getAcademicServiceRequest() {
	return academicServiceRequest;
    }

    public void setAcademicServiceRequest(PhdAcademicServiceRequest academicServiceRequest) {
	this.academicServiceRequest = academicServiceRequest;
    }

    public AcademicServiceRequestSituationType getSituationType() {
	return situationType;
    }

    public void setSituationType(AcademicServiceRequestSituationType situationType) {
	this.situationType = situationType;
    }

    public DateTime getWhenNewSituationOccured() {
	return whenNewSituationOccured;
    }

    public void setWhenNewSituationOccured(DateTime whenNewSituationOccured) {
	this.whenNewSituationOccured = whenNewSituationOccured;
    }

    public String getJustification() {
	return justification;
    }

    public void setJustification(final String justification) {
	this.justification = justification;
    }
}
