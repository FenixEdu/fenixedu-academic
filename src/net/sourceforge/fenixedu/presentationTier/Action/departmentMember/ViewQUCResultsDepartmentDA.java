package net.sourceforge.fenixedu.presentationTier.Action.departmentMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentTeacherDetailsBean;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.DepartmentUCResultsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/viewQucResults", module = "departmentMember")
@Forwards({ @Forward(name = "viewResumeResults", path = "/departmentMember/quc/viewResumeResults.jsp"),
	@Forward(name = "viewCompetenceResults", path = "/departmentMember/quc/viewCompetenceResults.jsp"),
	@Forward(name = "viewTeachersResults", path = "/departmentMember/quc/viewTeachersResults.jsp"),
	@Forward(name = "departmentUCView", path = "/departmentMember/quc/departmentUCView.jsp"),
	@Forward(name = "departmentTeacherView", path = "/departmentMember/quc/departmentTeacherView.jsp") })
public class ViewQUCResultsDepartmentDA extends ViewQUCResultsDA {

    @Override
    protected DepartmentUnit getDepartmentUnit(HttpServletRequest request) {
	DepartmentUnit departmentUnit = AccessControl.getPerson().getTeacher().getCurrentWorkingDepartment().getDepartmentUnit();
	return departmentUnit;
    }

    public ActionForward saveTeacherComment(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final DepartmentTeacherDetailsBean departmentTeacherDetailsBean = getRenderedObject("departmentTeacherDetailsBean");
	departmentTeacherDetailsBean.saveComment();

	RenderUtils.invalidateViewState();
	request.setAttribute("executionSemesterOID", departmentTeacherDetailsBean.getExecutionSemester().getExternalId());

	if (departmentTeacherDetailsBean.isBackToResume()) {
	    return resumeResults(actionMapping, actionForm, request, response);
	} else {
	    return teachersResults(actionMapping, actionForm, request, response);
	}
    }

    public ActionForward saveExecutionCourseComment(ActionMapping actionMapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	final DepartmentUCResultsBean departmentUCResultsBean = getRenderedObject("departmentUCResultsBean");
	departmentUCResultsBean.saveComment();

	RenderUtils.invalidateViewState();
	request.setAttribute("executionSemesterOID", departmentUCResultsBean.getExecutionCourse().getExecutionPeriod()
		.getExternalId());

	if (departmentUCResultsBean.isBackToResume()) {
	    return resumeResults(actionMapping, actionForm, request, response);
	} else {
	    return competenceResults(actionMapping, actionForm, request, response);
	}
    }

    @Override
    public boolean getShowAllComments() {
	return false;
    }
}
