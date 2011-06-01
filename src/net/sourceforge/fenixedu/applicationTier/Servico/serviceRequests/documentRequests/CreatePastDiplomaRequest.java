package net.sourceforge.fenixedu.applicationTier.Servico.serviceRequests.documentRequests;

import net.sourceforge.fenixedu.applicationTier.factoryExecutors.DocumentRequestCreator;
import net.sourceforge.fenixedu.dataTransferObject.accounting.AccountingTransactionDetailDTO;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.accounting.PaymentMode;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.PastDegreeDiplomaRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.AcademicServiceRequestSituationType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.services.Service;

public class CreatePastDiplomaRequest {

    @Service
    public static DiplomaRequest create(DocumentRequestCreator bean) {
	if (bean.getRegistration().getStartDate().isAfter(bean.getPastRequestDate())) {
	    throw new DomainException("DiplomaRequest.cannot.request.before.registration.start");
	}

	bean.setRequestDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
	DiplomaRequest diploma = createPastDiplomaRequest(bean);
	diploma.getActiveSituation().setSituationDate(bean.getPastRequestDate().toDateTimeAtStartOfDay());
	createSituations(diploma, bean);
	return diploma;
    }

    private static DiplomaRequest createPastDiplomaRequest(DocumentRequestCreator bean) {
	DiplomaRequest request = new DiplomaRequest();
	request.init(bean);
	return request;
    }

    private static void createSituations(DiplomaRequest diploma, DocumentRequestCreateBean bean) {
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

	createPaymentSituation(diploma, bean);
	process(diploma, bean.getPastRequestDate());
	diploma.setNumberOfPages(1);
	send(diploma, bean.getPastRequestDate());
	receive(diploma, bean.getPastRequestDate());
	conclude(diploma, bean.getPastEmissionDate());
	delivered(diploma, bean.getPastDispatchDate());
    }

    private static void createPaymentSituation(DiplomaRequest diploma, DocumentRequestCreateBean bean) {
	if (isPayed(bean)) {
	    PastDegreeDiplomaRequestEvent event = new PastDegreeDiplomaRequestEvent(diploma.getAdministrativeOffice(),
		    diploma.getPerson(), diploma, bean.getPastPaymentAmount());

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

    private static void process(DiplomaRequest diploma, LocalDate processDate) {
	editSituation(diploma, AcademicServiceRequestSituationType.PROCESSING, processDate.toDateTimeAtStartOfDay()
		.plusMinutes(1));
    }

    private static void send(DiplomaRequest diploma, LocalDate conclusionDate) {
	editSituation(diploma, AcademicServiceRequestSituationType.SENT_TO_EXTERNAL_ENTITY, conclusionDate
		.toDateTimeAtStartOfDay().plusMinutes(2));
    }

    private static void receive(DiplomaRequest diploma, LocalDate conclusionDate) {
	editSituation(diploma, AcademicServiceRequestSituationType.RECEIVED_FROM_EXTERNAL_ENTITY, conclusionDate
		.toDateTimeAtStartOfDay().plusMinutes(3));
    }

    private static void conclude(DiplomaRequest diploma, LocalDate conclusionDate) {
	editSituation(diploma, AcademicServiceRequestSituationType.CONCLUDED, conclusionDate.toDateTimeAtStartOfDay()
		.plusMinutes(4));
    }

    private static void delivered(DiplomaRequest diploma, LocalDate deliveryDate) {
	editSituation(diploma, AcademicServiceRequestSituationType.DELIVERED, deliveryDate.toDateTimeAtStartOfDay()
		.plusMinutes(5));
    }

    private static void editSituation(DiplomaRequest diploma, AcademicServiceRequestSituationType situationType,
	    DateTime situationDate) {
	final AcademicServiceRequestBean bean = new AcademicServiceRequestBean(situationType, AccessControl.getPerson()
		.getEmployee());
	bean.setPastDiplomaRequest(true);
	bean.setFinalSituationDate(situationDate);
	bean.setJustification("-");
	diploma.edit(bean);
    }

}
