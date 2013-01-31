package net.sourceforge.fenixedu.presentationTier.Action.manager.operator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(
		module = "operator",
		path = "/manageEnrolementPeriods",
		input = "/manageEnrolementPeriods.do?method=prepare&page=0",
		attribute = "enrolementPeriodsForm",
		formBean = "enrolementPeriodsForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(name = "editEnrolmentInstructions", path = "/manager/editEnrolmentInstructions.jsp"),
		@Forward(name = "showEnrolementPeriods", path = "/manager/enrolementPeriods.jsp") })
public class ManageEnrolementPeriodsDAForOperator extends
		net.sourceforge.fenixedu.presentationTier.Action.manager.ManageEnrolementPeriodsDA {
}