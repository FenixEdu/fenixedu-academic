package net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits.teacher;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "teacher", path = "/credits", attribute = "teacherCreditsForm", formBean = "teacherCreditsForm",
        scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "show-teacher-credits", path = "/teacher/credits.jsp", tileProperties = @Tile(
                navLocal = "/teacher/commons/navigationBarIndex.jsp")),
        @Forward(name = "teacher-not-found", path = "/showAllTeacherCreditsResume.do?method=showTeacherCreditsResume&page=0") })
public class DepartmentMemberShowTeacherCreditsDispatchActionForTeacher
        extends
        net.sourceforge.fenixedu.presentationTier.Action.departmentMember.credits.DepartmentMemberShowTeacherCreditsDispatchAction {
}