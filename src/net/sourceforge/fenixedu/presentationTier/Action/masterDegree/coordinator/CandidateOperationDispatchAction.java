package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class CandidateOperationDispatchAction extends FenixDispatchAction {

    /** request params * */
    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward getCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	IUserView userView = getUserView(request);

	Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanID"));

	List candidates = null;
	Object args[] = { degreeCurricularPlanId };

	try {
	    candidates = (List) ServiceManagerServiceFactory.executeService("ReadDegreeCandidates", args);
	} catch (FenixServiceException e) {
	    throw new FenixActionException(e);
	}

	if (candidates.size() == 0)
	    throw new NonExistingActionException("error.exception.nonExistingCandidates", "", null);

	request.setAttribute("masterDegreeCandidateList", candidates);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

	return mapping.findForward("ViewList");

    }

    public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException, FenixFilterException {

	IUserView userView = UserView.getUser();

	Integer degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
	Integer candidateID = Integer.valueOf(request.getParameter("candidateID"));

	Object[] args = { candidateID };

	InfoMasterDegreeCandidate infoMasterDegreeCandidate;
	try {
	    infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory.executeService(
		    "ReadMasterDegreeCandidateByID", args);
	} catch (FenixServiceException e) {
	    e.printStackTrace();
	    throw new FenixActionException();
	}

	List candidateStudyPlan = getCandidateStudyPlanByCandidateID(candidateID, userView);

	request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
	request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
	request.setAttribute("candidateStudyPlan", candidateStudyPlan);

	return mapping.findForward("ActionReady");
    }

    /**
     * 
     * @author Ricardo Clerigo & Telmo Nabais
     * @param candidateID
     * @param userView
     * @return
     */
    private ArrayList getCandidateStudyPlanByCandidateID(Integer candidateID, IUserView userView) {
	Object[] args = { candidateID };

	try {
	    return (ArrayList) ServiceManagerServiceFactory.executeService("ReadCandidateEnrolmentsByCandidateID", args);
	} catch (Exception e) {
	    return null;
	}
    }

}