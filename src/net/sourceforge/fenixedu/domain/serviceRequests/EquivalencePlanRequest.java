package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.RegistrationAcademicServiceRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.EquivalencePlanRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.AcademicServiceRequestType;

import org.apache.log4j.Logger;

public class EquivalencePlanRequest extends EquivalencePlanRequest_Base {
    private static final Logger logger = Logger.getLogger(EquivalencePlanRequest.class);

    private static final String COURSE_LABEL = "Nome da disciplina do currículo de Bolonha";
    private static final String COURSE_ECTS = "ECTS";
    private static final String EQUIVALENT_COURSE_LABEL = "Nome da(s) disciplina(s) considerada(s) equivalente(s)";
    private static final String GRADE_LABEL = "Classificação";
    private static final String GRADE_SCALE = "Escala";
    private static final String MEC2006 = "MEC 2006"; // remove after when the
						      // process is open to all
						      // degrees

    protected EquivalencePlanRequest() {
	super();
    }

    public EquivalencePlanRequest(final RegistrationAcademicServiceRequestCreateBean bean) {
	this();
	super.init(bean);

	checkParameters(bean);
	setNumberOfEquivalences(bean.getNumberOfEquivalences());
    }

    private void checkParameters(final RegistrationAcademicServiceRequestCreateBean bean) {
	if (bean.getExecutionYear() == null) {
	    throw new DomainException("error.EquivalencePlanRequest.executionYear.cannot.be.null");
	}
    }

    @Override
    protected void checkRegistrationStartDate(RegistrationAcademicServiceRequestCreateBean bean) {

    }

    @Override
    public AcademicServiceRequestType getAcademicServiceRequestType() {
	return AcademicServiceRequestType.EQUIVALENCE_PLAN;
    }

    @Override
    public EventType getEventType() {
	return EventType.EQUIVALENCE_PLAN_REQUEST;
    }

    @Override
    protected void createAcademicServiceRequestSituations(AcademicServiceRequestBean academicServiceRequestBean) {
	super.createAcademicServiceRequestSituations(academicServiceRequestBean);

	if (academicServiceRequestBean.isNew()) {
	    if (!isFree()) {
		new EquivalencePlanRequestEvent(getAdministrativeOffice(), getPerson(), this);
	    }
	}
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestBean academicServiceRequestBean) {
	if (academicServiceRequestBean.isToCancelOrReject() && hasEvent()) {
	    getEvent().cancel(academicServiceRequestBean.getResponsible());

	} else if (academicServiceRequestBean.isToProcess()) {
	    if (isPayable() && !isPayed()) {
		throw new DomainException("AcademicServiceRequest.hasnt.been.payed");
	    }
	    academicServiceRequestBean.setSituationDate(getActiveSituation().getSituationDate().toYearMonthDay());
	}
    }

    @Override
    protected void checkRulesToDelete() {
	if (hasAnyEquivalencePlanRevisionRequests()) {
	    throw new DomainException("error.AcademicServiceRequest.cannot.be.deleted");
	}
    }

    @Override
    public boolean isToPrint() {
	return false;
    }

    @Override
    public boolean isPossibleToSendToOtherEntity() {
	return true;
    }

    @Override
    public boolean isManagedWithRectorateSubmissionBatch() {
	return false;
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return true;
    }

    @Override
    public boolean isPayedUponCreation() {
	return true;
    }

    @Override
    public boolean hasPersonalInfo() {
	return false;
    }
}
