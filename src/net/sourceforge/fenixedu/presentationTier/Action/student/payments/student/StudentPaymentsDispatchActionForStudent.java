package net.sourceforge.fenixedu.presentationTier.Action.student.payments.student;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "student",
		path = "/payments",
		attribute = "paymentsForm",
		formBean = "paymentsForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "showEvents", path = "payments.management.showEvents"),
		@Forward(name = "showEventDetails", path = "payments.management.showEventDetails") })
public class StudentPaymentsDispatchActionForStudent extends
		net.sourceforge.fenixedu.presentationTier.Action.student.payments.StudentPaymentsDispatchAction {
}