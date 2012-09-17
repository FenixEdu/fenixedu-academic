package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageCreditsReductionsDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/creditsReductions", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "editReductionService", path = "/credits/degreeTeachingService/editCreditsReduction.jsp"),
	@Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class DepartmentMemberManageCreditsReductionsDA extends ManageCreditsReductionsDispatchAction {
}
