package net.sourceforge.fenixedu.presentationTier.Action.manager.payments;

import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.payments.PaymentsManagementDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/paymentsManagement", module = "manager")
@Forwards({
        @Forward(name = "searchPersons", path = "/manager/payments/events/searchPersons.jsp"),
        @Forward(name = "showEvents", path = "/manager/payments/events/showEvents.jsp"),
        @Forward(name = "editCancelEventJustification", path = "/manager/payments/events/editCancelEventJustification.jsp"),
        @Forward(name = "showPaymentsForEvent", path = "/manager/payments/events/showPaymentsForEvent.jsp"),
        @Forward(name = "chooseTargetEventForPaymentsTransfer",
                path = "/manager/payments/events/chooseTargetEventForPaymentsTransfer.jsp"),
        @Forward(name = "annulTransaction", path = "/manager/payments/events/annulTransaction.jsp"),
        @Forward(name = "showOperations", path = "/manager/payments/showOperations.jsp"),
        @Forward(name = "showReceipts", path = "/manager/payments/receipts/showReceipts.jsp"),
        @Forward(name = "showReceipt", path = "/manager/payments/receipts/showReceipt.jsp"),
        @Forward(name = "depositAmount", path = "/manager/payments/events/depositAmount.jsp"),
        @Forward(name = "viewCodes", path = "/manager/payments/codes/viewCodes.jsp"),
        @Forward(name = "createPaymentCodeMapping", path = "/manager/payments/codes/createPaymentCodeMapping.jsp"),
        @Forward(name = "changePaymentPlan", path = "/manager/payments/events/changePaymentPlan.jsp"),
        @Forward(name = "viewEventsForCancellation", path = "/manager/payments/events/viewEventsForCancellation.jsp") })
public class PaymentsManagementDAForManager extends PaymentsManagementDA {
}
