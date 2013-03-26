package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.payments;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.predicates.AcademicPredicates;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/paymentsManagement", module = "academicAdministration")
@Forwards({
        @Forward(name = "searchPersons", path = "/academicAdministration/payments/events/searchPersons.jsp"),
        @Forward(name = "showEvents", path = "/academicAdministration/payments/events/showEvents.jsp"),
        @Forward(name = "editCancelEventJustification", path = "/manager/payments/events/editCancelEventJustification.jsp"),
        @Forward(name = "showPaymentsForEvent", path = "/academicAdministration/payments/events/showPaymentsForEvent.jsp"),
        @Forward(name = "chooseTargetEventForPaymentsTransfer",
                path = "/academicAdministration/payments/events/chooseTargetEventForPaymentsTransfer.jsp"),
        @Forward(name = "annulTransaction", path = "/academicAdministration/payments/events/annulTransaction.jsp"),
        @Forward(name = "showOperations", path = "/academicAdministration/payments/showOperations.jsp"),
        @Forward(name = "showReceipts", path = "/academicAdministration/payments/receipts/showReceipts.jsp"),
        @Forward(name = "showReceipt", path = "/academicAdministration/payments/receipts/showReceipt.jsp"),
        @Forward(name = "depositAmount", path = "/academicAdministration/payments/events/depositAmount.jsp"),
        @Forward(name = "viewCodes", path = "/academicAdministration/payments/codes/viewCodes.jsp"),
        @Forward(name = "createPaymentCodeMapping", path = "/academicAdministration/payments/codes/createPaymentCodeMapping.jsp"),
        @Forward(name = "changePaymentPlan", path = "/academicAdministration/payments/events/changePaymentPlan.jsp"),
        @Forward(name = "viewEventsForCancellation",
                path = "/academicAdministration/payments/events/viewEventsForCancellation.jsp") })
public class PaymentsManagementDAForAcademicAdmin extends PaymentsManagementDA {

    @Override
    public ActionForward showOperations(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        request.setAttribute("permission", getPermissionForAcademicAdministration(getPerson(request)));
        request.setAttribute("person", getPerson(request));

        return mapping.findForward("showOperations");
    }

    private boolean getPermissionForAcademicAdministration(Person person) {
        return AcademicPredicates.MANAGE_STUDENT_PAYMENTS_ADV.evaluate(person);
    }
}
