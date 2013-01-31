package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.credits.util.DepartmentCreditsBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(module = "scientificCouncil", path = "/projectTutorialCourses", scope = "request", parameter = "method")
@Forwards(value = { @Forward(
		name = "showDepartmentExecutionCourses",
		path = "/credits/showDepartmentExecutionCourses.jsp",
		tileProperties = @Tile(title = "private.scientificcouncil.credits.coursestypes")) })
public class ProjectTutorialExecutionCoursesDA extends FenixDispatchAction {

	public ActionForward showDepartmentExecutionCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {
		DepartmentCreditsBean departmentCreditsBean = getRenderedObject();
		if (departmentCreditsBean == null) {
			departmentCreditsBean = new DepartmentCreditsBean();
			departmentCreditsBean.setAvailableDepartments(new ArrayList<Department>(rootDomainObject.getDepartments()));
		}
		request.setAttribute("departmentCreditsBean", departmentCreditsBean);
		return mapping.findForward("showDepartmentExecutionCourses");
	}

	public ActionForward changeExecutionCourseType(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws NumberFormatException, FenixFilterException, FenixServiceException {

		ExecutionCourse executionCourse =
				AbstractDomainObject.fromExternalId((String) getFromRequest(request, "executionCourseOid"));
		executionCourse.changeProjectTutorialCourse();
		Department department = AbstractDomainObject.fromExternalId((String) getFromRequest(request, "departmentOid"));
		DepartmentCreditsBean departmentCreditsBean =
				new DepartmentCreditsBean(department, new ArrayList<Department>(rootDomainObject.getDepartments()));
		request.setAttribute("departmentCreditsBean", departmentCreditsBean);
		return mapping.findForward("showDepartmentExecutionCourses");
	}

}