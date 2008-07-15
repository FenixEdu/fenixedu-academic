package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.payments;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.commons.administrativeOffice.payments.CreditNotesManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/creditNotes", module = "academicAdminOffice", formBeanClass = CreditNotesManagementDA.CreditNotesActionForm.class)
@Forwards( { @Forward(name = "list", path = "/academicAdminOffice/payments/creditNotes/listCreditNotes.jsp"),
	@Forward(name = "create", path = "/academicAdminOffice/payments/creditNotes/createCreditNote.jsp"),
	@Forward(name = "show", path = "/academicAdminOffice/payments/creditNotes/showCreditNote.jsp"),
	@Forward(name = "prepareShowReceipt", path = "/receipts.do?method=prepareShowReceipt")

})
public class AcademicAdminOfficeCreditNotesManagementDA extends CreditNotesManagementDA {

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
