/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByCurricularCourseParametersBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class StudentsListByCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepareByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {
	request.setAttribute("searchBean", new SearchStudentsByCurricularCourseParametersBean());
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward chooseExecutionYearPostBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	Object searchBean = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchBean", searchBean);

	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final SearchStudentsByCurricularCourseParametersBean searchBean = (SearchStudentsByCurricularCourseParametersBean) getRenderedObject();

	final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>) executeService(
		"ReadCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", searchBean.getDegreeCurricularPlan()
			.getIdInternal(), searchBean.getExecutionYear().getIdInternal());

	if (degreeModuleScopes.isEmpty()) {
	    addActionMessage("message", request, "error.nonExisting.AssociatedCurricularCourses");
	} else {
	    request.setAttribute("degreeModuleScopes", degreeModuleScopes);
	}

	request.setAttribute("searchBean", searchBean);
	return mapping.findForward("chooseCurricularCourse");
    }

    public ActionForward searchByCurricularCourse(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final CurricularCourse curricularCourse = (CurricularCourse) rootDomainObject
		.readDegreeModuleByOID(getIntegerFromRequest(request, "curricularCourseCode"));
	final Integer semester = getIntegerFromRequest(request, "semester");
	final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(getIntegerFromRequest(request,
		"executionYearID"));

	final List<Enrolment> enrolments = (List<Enrolment>) executeService("SearchStudentByCriteria", executionYear,
		curricularCourse, semester);

	request.setAttribute("semester", semester);
	request.setAttribute("year", getIntegerFromRequest(request, "year"));
	request.setAttribute("enrolmentList", enrolments);
	request.setAttribute("searchBean", new SearchStudentsByCurricularCourseParametersBean(executionYear, curricularCourse));

	return mapping.findForward("studentByCurricularCourse");
    }

}
