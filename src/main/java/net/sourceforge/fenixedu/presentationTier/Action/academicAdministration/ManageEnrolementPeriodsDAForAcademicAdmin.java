package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "academicAdministration", path = "/manageEnrolementPeriods",
        input = "/manageEnrolementPeriods.do?method=prepare&page=0", attribute = "enrolementPeriodsForm",
        formBean = "enrolementPeriodsForm", scope = "request", parameter = "method")
@Forwards({
        @Forward(name = "editEnrolmentInstructions",
                path = "/academicAdministration/enrolmentPeriodManagement/editEnrolmentInstructions.jsp"),
        @Forward(name = "showEnrolementPeriods", path = "/academicAdministration/enrolmentPeriodManagement/enrolementPeriods.jsp"),
        @Forward(name = "createPeriod", path = "/academicAdministration/enrolmentPeriodManagement/createPeriod.jsp"),
        @Forward(name = "changePeriodValues", path = "/academicAdministration/enrolmentPeriodManagement/changePeriodValues.jsp") })
public class ManageEnrolementPeriodsDAForAcademicAdmin extends ManageEnrolementPeriodsDA {
}