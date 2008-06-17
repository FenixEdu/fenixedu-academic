package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.OtherPartyPaymentManagementDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

@Mapping(path = "/otherPartyPayments", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "showEvents", path = "/academicAdminOffice/payments/otherPartyPayment/showEventsForOtherPartyPayment.jsp"),
	@Forward(name = "showPaymentsForEvent", path = "/academicAdminOffice/payments/otherPartyPayment/showOtherPartyPaymentsForEvent.jsp"),
	@Forward(name = "prepareCreate", path = "/academicAdminOffice/payments/otherPartyPayment/prepareCreateOtherPartyPayment.jsp"),
	@Forward(name = "confirmCreate", path = "/academicAdminOffice/payments/otherPartyPayment/confirmCreateOtherPartyPayment.jsp"),
	@Forward(name = "showGuide", path = "/academicAdminOffice/payments/otherPartyPayment/showGuideForOtherPartyPayment.jsp"),
	@Forward(name = "printGuide", path = "/payments/otherPartyPayment/printGuideForOtherPartyPayment.jsp", useTile = false),
	@Forward(name = "showOperations", path = "/payments.do?method=showOperations")

})
public class AcademicAdminOfficeOtherPartyPaymentsManagementDA extends OtherPartyPaymentManagementDA {

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
