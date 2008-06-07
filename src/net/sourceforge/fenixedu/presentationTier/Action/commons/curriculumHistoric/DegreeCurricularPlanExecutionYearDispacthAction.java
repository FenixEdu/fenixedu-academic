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

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

/**
 * @author nmgo
 * @author lmre
 */
public class DegreeCurricularPlanExecutionYearDispacthAction extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	request.setAttribute("executionDegreeBean", new ExecutionDegreeListBean());

	return mapping.findForward("chooseExecutionYear");
    }

    public ActionForward chooseDegreeCurricularPlan(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request, final HttpServletResponse response) {
	final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject();
	executionDegreeBean.setDegreeCurricularPlan(null);

	RenderUtils.invalidateViewState();

	request.setAttribute("executionDegreeBean", executionDegreeBean);

        return mapping.findForward("chooseExecutionYear");
    }

    @SuppressWarnings("unchecked")
    public ActionForward showActiveCurricularCourseScope(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

	final ExecutionDegreeListBean executionDegreeBean = (ExecutionDegreeListBean) getRenderedObject("executionYear");

	final Object[] args = {
			executionDegreeBean.getDegreeCurricularPlan().getIdInternal(),
			executionDegreeBean.getExecutionYear().getIdInternal() };

	final SortedSet<DegreeModuleScope> degreeModuleScopes = (SortedSet<DegreeModuleScope>)
                executeService(request, "ReadActiveCurricularCourseScopeByDegreeCurricularPlanAndExecutionYear", args);

        final ActionErrors errors = new ActionErrors();
        if (degreeModuleScopes.isEmpty()) {
            errors.add("noDegreeCurricularPlan", new ActionError("error.nonExisting.AssociatedCurricularCourses"));
            saveErrors(request, errors);            
        } else {
            request.setAttribute("degreeModuleScopes", degreeModuleScopes);
        }

        request.setAttribute("executionYearID", executionDegreeBean.getExecutionYear().getIdInternal());
        request.setAttribute("degreeCurricularPlan", executionDegreeBean.getDegreeCurricularPlan());

        return mapping.findForward("showActiveCurricularCourses");
    }

}
