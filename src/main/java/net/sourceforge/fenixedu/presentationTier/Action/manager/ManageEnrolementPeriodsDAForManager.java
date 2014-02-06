package net.sourceforge.fenixedu.presentationTier.Action.manager;

import net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.ManageEnrolementPeriodsDA;
import net.sourceforge.fenixedu.presentationTier.Action.manager.ManagerApplications.ExecutionsManagementApp;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = ExecutionsManagementApp.class, descriptionKey = "title.manage.enrolement.period",
        path = "enrolement-period", titleKey = "title.manage.enrolement.period")
@Mapping(module = "manager", path = "/manageEnrolementPeriods", input = "/manageEnrolementPeriods.do?method=prepare&page=0",
        attribute = "enrolementPeriodsForm", formBean = "enrolementPeriodsForm", scope = "request", parameter = "method")
@Forwards({
        @Forward(name = "editEnrolmentInstructions", path = "/manager/enrolmentPeriodManagement/editEnrolmentInstructions.jsp"),
        @Forward(name = "showEnrolementPeriods", path = "/manager/enrolmentPeriodManagement/enrolementPeriods.jsp"),
        @Forward(name = "createPeriod", path = "/manager/enrolmentPeriodManagement/createPeriod.jsp"),
        @Forward(name = "changePeriodValues", path = "/manager/enrolmentPeriodManagement/changePeriodValues.jsp") })
public class ManageEnrolementPeriodsDAForManager extends ManageEnrolementPeriodsDA {
}