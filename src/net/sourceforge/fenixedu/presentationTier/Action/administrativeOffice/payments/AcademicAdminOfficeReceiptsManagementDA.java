package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.ReceiptsManagementDA;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forward;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Forwards;
import net.sourceforge.fenixedu.presentationTier.struts.annotations.Mapping;

@Mapping(path = "/receipts", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( {
	@Forward(name = "showReceipts", path = "/academicAdminOffice/payments/receipts/showReceipts.jsp"),
	@Forward(name = "showReceipt", path = "/academicAdminOffice/payments/receipts/showReceipt.jsp"),
	@Forward(name = "showPaymentsWithoutReceipt", path = "/academicAdminOffice/payments/receipts/showPaymentsWithoutReceipt.jsp"),
	@Forward(name = "confirmCreateReceipt", path = "/academicAdminOffice/payments/receipts/confirmCreateReceipt.jsp"),
	@Forward(name = "showOperations", path = "/payments.do?method=showOperations"),
	@Forward(name = "editReceipt", path = "/academicAdminOffice/payments/receipts/editReceipt.jsp")

})
public class AcademicAdminOfficeReceiptsManagementDA extends ReceiptsManagementDA {

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
