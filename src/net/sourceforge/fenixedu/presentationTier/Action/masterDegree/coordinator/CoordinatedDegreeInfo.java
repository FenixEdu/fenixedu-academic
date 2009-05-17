/**
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : -Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 * 
 * modified by Fernanda Quitério
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator.ReadDegreeCandidates;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CoordinatedDegreeInfo extends FenixAction {

    public static void setCoordinatorContext(final HttpServletRequest request) {
	final Integer degreeCurricularPlanOID = findDegreeCurricularPlanID(request);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

	if (degreeCurricularPlanOID != null) {
	    final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject.readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
	    if (degreeCurricularPlan != null) {
		final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

		final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
		request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);
		
		final List<InfoMasterDegreeCandidate> infoMasterDegreeCandidates = (List) ReadDegreeCandidates.run(degreeCurricularPlanOID);
		request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT, Integer.valueOf(infoMasterDegreeCandidates.size()));
	    }
	}
    }

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws FenixActionException {
	setCoordinatorContext(request);
	return mapping.findForward("Success");
    }

    private static Integer findDegreeCurricularPlanID(HttpServletRequest request) {
	final Integer degreeCurricularPlanID;

	String paramValue = request.getParameter("degreeCurricularPlanID");
	if (paramValue != null) {
	    degreeCurricularPlanID = Integer.valueOf(paramValue);
	} else {
	    paramValue = (String) request.getAttribute("degreeCurricularPlanID");
	    degreeCurricularPlanID = paramValue == null ? null : Integer.valueOf(paramValue);
	}

	return degreeCurricularPlanID;
    }

}