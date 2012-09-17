package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.credits.ManageTeacherServiceCommentsDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/teacherServiceComments", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "editTeacherServiceComment", path = "/credits/degreeTeachingService/editTeacherServiceComment.jsp"),
	@Forward(name = "viewAnnualTeachingCredits", path = "/credits.do?method=viewAnnualTeachingCredits") })
public class DepartmentMemberManageTeacherServiceCommentsDispatchAction extends ManageTeacherServiceCommentsDispatchAction {
}
