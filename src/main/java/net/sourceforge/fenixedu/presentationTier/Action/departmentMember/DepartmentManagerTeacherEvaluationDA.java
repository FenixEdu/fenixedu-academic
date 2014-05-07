package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.departmentMember.DepartmentMemberApp.DepartmentMemberPresidentApp;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.evaluation.TeacherEvaluationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@StrutsFunctionality(app = DepartmentMemberPresidentApp.class, path = "teacher-evaluation",
        titleKey = "label.teacher.evaluation.title", bundle = "ResearcherResources")
@Mapping(module = "departmentMember", path = "/teacherEvaluation")
public class DepartmentManagerTeacherEvaluationDA extends TeacherEvaluationDA {

    @Override
    @EntryPoint
    public ActionForward viewManagementInterface(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return super.viewManagementInterface(mapping, form, request, response);
    }

}
