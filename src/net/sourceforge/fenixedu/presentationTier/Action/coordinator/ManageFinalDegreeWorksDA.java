package net.sourceforge.fenixedu.presentationTier.Action.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

public class ManageFinalDegreeWorksDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws FenixFilterException, FenixServiceException {

	final DegreeCurricularPlan degreeCurricularPlan = readDegreeCurricularPlan(request);
	request.setAttribute("degreeCurricularPlan", degreeCurricularPlan);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlan.getIdInternal().toString());

	final DynaActionForm dynaActionForm = (DynaActionForm) form;
	final String executionDegreeIDString = dynaActionForm.getString("executionDegreeID");
	final ExecutionDegree executionDegree;
	if (executionDegreeIDString == null || executionDegreeIDString.length() == 0) {
	    executionDegree = findExecutionDegree(degreeCurricularPlan);
	    dynaActionForm.set("executionDegreeID", executionDegree.getIdInternal().toString());
	} else {
	    executionDegree = findExecutionDegree(degreeCurricularPlan, Integer.valueOf(executionDegreeIDString));
	}
	request.setAttribute("executionDegree", executionDegree);

	return mapping.findForward("show-final-degree-works-managment-page");
    }

    private ExecutionDegree findExecutionDegree(final DegreeCurricularPlan degreeCurricularPlan, final Integer executionDegreeID) {
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
	    if (executionDegree.getIdInternal().equals(executionDegreeID)) {
		return executionDegree;
	    }
	}
	return null;
    }

    private ExecutionDegree findExecutionDegree(final DegreeCurricularPlan degreeCurricularPlan) {
	ExecutionDegree mostRecentExecutionDegree = null;
	for (final ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
	    final ExecutionYear executionYear = executionDegree.getExecutionYear();
	    if (executionYear.isCurrent()) {
		return executionDegree;
	    }
	    if (mostRecentExecutionDegree == null || mostRecentExecutionDegree.getExecutionYear().compareTo(executionYear) < 0) {
		mostRecentExecutionDegree = executionDegree;
	    }
	}
	return mostRecentExecutionDegree;
    }

    private DegreeCurricularPlan readDegreeCurricularPlan(final HttpServletRequest request) throws FenixFilterException,
	    FenixServiceException {
	String degreeCurricularPlanIDString = request.getParameter("degreeCurricularPlanID");
	if (degreeCurricularPlanIDString == null || degreeCurricularPlanIDString.length() == 0) {
	    degreeCurricularPlanIDString = (String) request.getAttribute("degreeCurricularPlanID");
	}
	final Integer degreeCurricularPlanID = (degreeCurricularPlanIDString != null && degreeCurricularPlanIDString.length() > 0) ? Integer
		.valueOf(degreeCurricularPlanIDString)
		: null;
	return (DegreeCurricularPlan) readDomainObject(request, DegreeCurricularPlan.class, degreeCurricularPlanID);
    }

}