package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.PaymentsManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

@Mapping(path = "/payments", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "showOperations", path = "/academicAdminOffice/payments/showOperations.jsp"),
	@Forward(name = "showEvents", path = "/academicAdminOffice/payments/showEvents.jsp"),
	@Forward(name = "showEventsWithPayments", path = "/academicAdminOffice/payments/showEventsWithPayments.jsp"),
	@Forward(name = "showPaymentsForEvent", path = "/academicAdminOffice/payments/showPaymentsForEvent.jsp"),
	@Forward(name = "preparePayment", path = "/academicAdminOffice/payments/preparePayment.jsp"),
	@Forward(name = "preparePrintGuide", path = "/guides.do?method=preparePrintGuide"),
	@Forward(name = "showReceipt", path = "/receipts.do?method=prepareShowReceipt")

})
public class AcademicAdminOfficePaymentsManagementDispatchAction extends PaymentsManagementDispatchAction {

    @Override
    protected AdministrativeOffice getAdministrativeOffice(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getAdministrativeOffice();
    }

    @Override
    protected Unit getReceiptOwnerUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

    @Override
    protected Unit getReceiptCreatorUnit(HttpServletRequest request) {
	return getUserView(request).getPerson().getEmployee().getCurrentWorkingPlace();
    }

}
