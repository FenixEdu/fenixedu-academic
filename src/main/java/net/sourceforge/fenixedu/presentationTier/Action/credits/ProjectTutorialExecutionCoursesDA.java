package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.ScientificCouncilApplication.ScientificCreditsApp;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@StrutsFunctionality(app = ScientificCreditsApp.class, path = "execution-course-types",
        titleKey = "label.executionCourses.types", bundle = "TeacherCreditsSheetResources")
@Mapping(module = "scientificCouncil", path = "/projectTutorialCourses")
@Forwards(@Forward(name = "showDepartmentExecutionCourses", path = "/credits/showDepartmentExecutionCourses.jsp"))
public class ProjectTutorialExecutionCoursesDA extends FenixDispatchAction {

    @EntryPoint
    public ActionForward showDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {
        DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
        if (departmentCreditsBean == null) {
            departmentCreditsBean = new DepartmentCreditsBean();
            departmentCreditsBean.setAvailableDepartments(Department.readActiveDepartments());
        }
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("showDepartmentExecutionCourses");
    }

    public ActionForward changeExecutionCourseType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixServiceException {

        ExecutionCourse executionCourse = FenixFramework.getDomainObject((String) getFromRequest(request, "executionCourseOid"));
        executionCourse.changeProjectTutorialCourse();
        Department department = FenixFramework.getDomainObject((String) getFromRequest(request, "departmentOid"));
        DepartmentCreditsBean departmentCreditsBean =
                new DepartmentCreditsBean(department, new ArrayList<Department>(rootDomainObject.getDepartmentsSet()));
        request.setAttribute("departmentCreditsBean", departmentCreditsBean);
        return mapping.findForward("showDepartmentExecutionCourses");
    }

}