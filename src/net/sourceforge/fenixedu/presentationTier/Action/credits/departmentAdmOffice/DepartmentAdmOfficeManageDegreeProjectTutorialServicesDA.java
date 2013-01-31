package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageDegreeProjectTutorialServicesDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/degreeProjectTutorialService", scope = "request", parameter = "method")
@Forwards(value = {
		@Forward(name = "show-project-tutorial-service", path = "/credits/degreeTeachingService/showProjectTutorialService.jsp"),
		@Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class DepartmentAdmOfficeManageDegreeProjectTutorialServicesDA extends ManageDegreeProjectTutorialServicesDispatchAction {

}
