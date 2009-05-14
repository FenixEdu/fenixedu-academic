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

import net.sourceforge.fenixedu.applicationTier.IUserView;
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

import pt.ist.fenixWebFramework.security.UserView;

public class CoordinatedDegreeInfo extends FenixAction {

    @Override
    @SuppressWarnings("unchecked")
    public ActionForward execute(final ActionMapping mapping, final ActionForm form, final HttpServletRequest request,
	    final HttpServletResponse response) throws FenixActionException {
	    final IUserView userView = UserView.getUser();

	    final Integer degreeCurricularPlanOID = findDegreeCurricularPlanID(request);
	    request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanOID);

	    final DegreeCurricularPlan degreeCurricularPlan = rootDomainObject
		    .readDegreeCurricularPlanByOID(degreeCurricularPlanOID);
	    final ExecutionDegree executionDegree = degreeCurricularPlan.getMostRecentExecutionDegree();

	    final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
	    request.setAttribute(PresentationConstants.MASTER_DEGREE, infoExecutionDegree);

	    final List<InfoMasterDegreeCandidate> infoMasterDegreeCandidates;
	    try {
		infoMasterDegreeCandidates = (List) ReadDegreeCandidates.run(degreeCurricularPlanOID);
	    } catch (Exception e) {
		throw new FenixActionException(e);
	    }
	    request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_AMMOUNT, Integer.valueOf(infoMasterDegreeCandidates
		    .size()));

	    return mapping.findForward("Success");
    }

    /**
     * @param request
     * @return
     */
    final private Integer findDegreeCurricularPlanID(HttpServletRequest request) {
	final Integer degreeCurricularPlanID;

	if (request.getParameter("degreeCurricularPlanID") != null) {
	    degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
	} else {
	    degreeCurricularPlanID = Integer.valueOf((String) request.getAttribute("degreeCurricularPlanID"));
	}

	return degreeCurricularPlanID;
    }

}