package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberPresidentApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationDA;

import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberPresidentApp.class, path = "teacher-evaluation",
        titleKey = "label.teacher.evaluation.title", bundle = "ResearcherResources")
@Mapping(module = "departmentMember", path = "/teacherEvaluation")
public class DepartmentManagerTeacherEvaluationDA extends TeacherEvaluationDA {

}
