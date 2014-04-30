package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.credits.departmentMember.DepartmentMemberViewTeacherCreditsDA;
import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits.DepartmentMemberShowTeacherCreditsDispatchAction;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentMember", path = "/showFullTeacherCreditsSheet", formBean = "teacherCreditsForm",
        functionality = DepartmentMemberViewTeacherCreditsDA.class)
@Forwards({
        @Forward(name = "show-teacher-credits", path = "/departmentMember/credits/showTeacherCredits.jsp"),
        @Forward(name = "teacher-not-found",
                path = "/departmentMember/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
public class DepartmentMemberShowTeacherCreditsDispatchActionForDepartmentMember extends
        DepartmentMemberShowTeacherCreditsDispatchAction {
}