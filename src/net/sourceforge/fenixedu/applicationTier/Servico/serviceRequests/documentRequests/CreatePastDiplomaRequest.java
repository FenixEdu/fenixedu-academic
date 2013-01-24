package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.applicationTier.factoryExecutors.DocumentRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PastDegreeDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.PastDiplomaRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class CreatePastDiplomaRequest {

    @Service
    public static PastDiplomaRequest create(DocumentRequestCreator bean) {
	if (bean.getRegistration().getStartDate().isAfter(bean.getPastRequestDate())) {
	    throw new DomainException("DiplomaRequest.cannot.request.before.registration.start");
	}

	bean.setRequestDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
	PastDiplomaRequest request = createPastDiplomaRequest(bean);
	request.getActiveSituation().setSituationDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
	createSituations(request, bean);
	return request;
    }

    private static PastDiplomaRequest createPastDiplomaRequest(DocumentRequestCreator bean) {
	PastDiplomaRequest request = new PastDiplomaRequest();
	request.init(bean);
	return request;
    }

    private static void createSituations(PastDiplomaRequest request, DocumentRequestCreateBean bean) {
	if (!bean.getRegistration().isRegistrationConclusionProcessed()) {
	    throw new DomainException("DiplomaRequest.diploma.cannot.be.concluded");
	}

	LocalDate latestDate = bean.getPastRequestDate();
	if (bean.getPastPaymentDate() == null) {
	    bean.setPastPaymentDate(latestDate);
	} else {
	    latestDate = (latestDate.compareTo(bean.getPastPaymentDate()) < 0) ? bean.getPastPaymentDate() : latestDate;
	}
	if (bean.getPastEmissionDate() == null) {
	    bean.setPastEmissionDate(latestDate);
	} else {
	    latestDate = (latestDate.compareTo(bean.getPastEmissionDate()) < 0) ? bean.getPastEmissionDate() : latestDate;
	}
	if (bean.getPastDispatchDate() == null) {
	    bean.setPastDispatchDate(latestDate);
	}

	createPaymentSituation(request, bean);
	process(request, bean.getPastRequestDate());
	request.setNumberOfPages(1);
	send(request, bean.getPastRequestDate());
	receive(request, bean.getPastRequestDate());
	conclude(request, bean.getPastEmissionDate());
	deliver(request, bean.getPastDispatchDate());
    }

    private static void createPaymentSituation(PastDiplomaRequest request, DocumentRequestCreateBean bean) {
	if (isPayed(bean)) {
	    PastDegreeDiplomaRequestEvent event = new PastDegreeDiplomaRequestEvent(request.getAdministrativeOffice(),
		    request.getPerson(), request, bean.getPastPaymentAmount());

	    event.depositAmount(AccessControl.getPerson().getUser(), bean.getPastPaymentAmount(),
		    createTransactionDetailDTO(bean));
	}
    }

    private static boolean isPayed(DocumentRequestCreateBean bean) {
	return bean.getPastPaymentAmount() != null && bean.getPastPaymentAmount().isPositive();
    }

    private static AccountingTransactionDetailDTO createTransactionDetailDTO(DocumentRequestCreateBean bean) {
	return new AccountingTransactionDetailDTO(bean.getPastPaymentDate().toDateTimeAtStartOfDay(), PaymentMode.CASH);
    }

    private static void process(PastDiplomaRequest request, LocalDate requestDate) {
	editSituation(request, AcademicServiceRequestSituationType.PROCESSING, requestDate.toDateTimeAtStartOfDay()
		.plusMinutes(1));
    }

    private static void send(PastDiplomaRequest request, LocalDate requestDate) {
	editSituation(request, AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, requestDate.toDateTimeAtStartOfDay()
		.plusMinutes(2));
    }

    private static void receive(PastDiplomaRequest request, LocalDate requestDate) {
	editSituation(request, AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY, requestDate
		.toDateTimeAtStartOfDay().plusMinutes(3));
    }

    private static void conclude(PastDiplomaRequest request, LocalDate emissionDate) {
	editSituation(request, AcademicServiceRequestSituationType.CONCLUDED, emissionDate.toDateTimeAtStartOfDay()
		.plusMinutes(4));
    }

    private static void deliver(PastDiplomaRequest request, LocalDate dispatchDate) {
	editSituation(request, AcademicServiceRequestSituationType.DELIVERED, dispatchDate.toDateTimeAtStartOfDay()
		.plusMinutes(5));
    }

    private static void editSituation(PastDiplomaRequest request, AcademicServiceRequestSituationType situationType,
	    DateTime situationDate) {
	final AcademicServiceRequestBean bean = new AcademicServiceRequestBean(situationType, AccessControl.getPerson());
	bean.setFinalSituationDate(situationDate);
	bean.setJustification("-");
	request.edit(bean);
    }

}
