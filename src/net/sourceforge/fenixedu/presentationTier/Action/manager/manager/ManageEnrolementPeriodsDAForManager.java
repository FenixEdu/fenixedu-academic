package net.sourceforge.fenixedu.presentationTier.Action.manager.manager;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "manager", path = "/manageEnrolementPeriods", input = "/manageEnrolementPeriods.do?method=prepare&page=0", attribute = "enrolementPeriodsForm", formBean = "enrolementPeriodsForm", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "editEnrolmentInstructions", path = "/manager/editEnrolmentInstructions.jsp"),
	@Forward(name = "showEnrolementPeriods", path = "/manager/enrolementPeriods.jsp"),
	@Forward(name = "createPeriod", path = "/manager/enrolmentPeriodManagement/createPeriod.jsp"),
	@Forward(name = "changePeriodValues", path = "/manager/enrolmentPeriodManagement/changePeriodValues.jsp") })
public class ManageEnrolementPeriodsDAForManager extends net.sourceforge.fenixedu.presentationTier.Action.manager.ManageEnrolementPeriodsDA {
}