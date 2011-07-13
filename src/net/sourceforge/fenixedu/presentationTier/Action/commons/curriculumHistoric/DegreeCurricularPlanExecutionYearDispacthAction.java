/*
 * Created on Oct 7, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.ExecutionDegreeListBean;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author nmgo
 * @author lmre
 */
@Mapping(path = "/chooseExecutionYearAndDegreeCurricularPlan", module = "academicAdminOffice", formBean = "executionYearDegreeCurricularPlanForm")
@Forwards( { @Forward(name = "chooseExecutionYear", path = "/commons/curriculumHistoric/chooseDegreeCPlanExecutionYear.jsp"),
	@Forward(name = "showActiveCurricularCourses", path = "/commons/curriculumHistoric/showActiveCurricularCourseScopes.jsp") })
public class DegreeCurricularPlanExecutionYearDispacthAction extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	request.setAttribute("executionDegreeBean", new ExecutionDegreeListBean());

	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegree(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) {
	final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");
	executionDegreeBean.setDegreeCurricularPlan(null);
	executionDegreeBean.setAcademicInterval(null);
	RenderUtils.invalidateViewState();
	request.setAttribute("executionDegreeBean", executionDegreeBean);
	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegreeCurricularPlan(final ActionMapping mapping, final ActionForm form,
	    final HttpServletRequest request, final HttpServletResponse response) {
	final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");
	executionDegreeBean.setAcademicInterval(null);
	RenderUtils.invalidateViewState();
	request.setAttribute("executionDegreeBean", executionDegreeBean);
	return mapping.findForward("chooseExecutionYear");
    }

    @SuppressWarnings("unchecked")
    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final ExecutionDegreeListBean executionDegreeBean = getRenderedObject("academicInterval");

	final Object[] args = { executionDegreeBean.getDegreeCurricularPlan().getIdInternal(),
		executionDegreeBean.getAcademicInterval() };

	final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>) executeService(
		"ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", args);

	final ActionErrors errors = new ActionErrors();
	if (degreeModuleScopes.isEmpty()) {
	    errors.add("noDegreeCurricularPlan", new ActionError("error.nonExisting.AssociatedCurricularCourses"));
	    saveErrors(request, errors);
	} else {
	    request.setAttribute("degreeModuleScopes", degreeModuleScopes);
	}

	request.setAttribute(PresentationConstants.ACADEMIC_INTERVAL, executionDegreeBean.getAcademicInterval());
	request.setAttribute("degreeCurricularPlan", executionDegreeBean.getDegreeCurricularPlan());

	return mapping.findForward("showActiveCurricularCourses");
    }

}
