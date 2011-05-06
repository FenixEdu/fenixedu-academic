package net.sourceforge.fenixedu.domain.phd.serviceRequests;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PhdAcademicServiceRequestBean implements Serializable, IPhdAcademicServiceRequest {

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

    @Service
    public void handleNewSituation() {

	switch (getSituationType()) {
	case PROCESSING:
	    getAcademicServiceRequest().process(getWhenNewSituationOccured().toYearMonthDay());
	    break;
	case CANCELLED:
	    getAcademicServiceRequest().cancel(getJustification());
	    break;
	case REJECTED:
	    getAcademicServiceRequest().reject(getJustification());
	    break;
	case RECEIVED_FROM_EXTERNAL_ENTITY:
	    getAcademicServiceRequest().receivedFromExternalEntity(getWhenNewSituationOccured().toYearMonthDay(),
		    getJustification());
	case CONCLUDED:
	    getAcademicServiceRequest().conclude(getWhenNewSituationOccured().toYearMonthDay(), getJustification());
	    break;
	case SENT_TO_EXTERNAL_ENTITY:
	    getAcademicServiceRequest().sendToExternalEntity(getWhenNewSituationOccured().toYearMonthDay(), getJustification());
	default:
	    throw new DomainException("error.PhdAcademicServiceRequestBean.unknown.situation.type");
	}
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	return academicServiceRequest.getPhdIndividualProgramProcess();
    }
}
