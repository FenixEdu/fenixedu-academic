package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadCandidateEnrolmentsByCandidateID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadMasterDegreeCandidateByID;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.coordinator.ReadDegreeCandidates;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

public class CandidateOperationDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    /** request params * */
    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward getCandidates(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        String degreeCurricularPlanId = request.getParameter("degreeCurricularPlanID");

        List candidates = null;

        candidates = ReadDegreeCandidates.run(degreeCurricularPlanId);

        if (candidates.size() == 0) {
            throw new NonExistingActionException("error.exception.nonExistingCandidates", "", null);
        }

        request.setAttribute("masterDegreeCandidateList", candidates);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

        return mapping.findForward("ViewList");

    }

    public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = UserView.getUser();

        String degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
        String candidateID = request.getParameter("candidateID");

        InfoMasterDegreeCandidate infoMasterDegreeCandidate;
        infoMasterDegreeCandidate = ReadMasterDegreeCandidateByID.run(candidateID);

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
    private ArrayList getCandidateStudyPlanByCandidateID(String candidateID, IUserView userView) {
        try {
            return (ArrayList) ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);
        } catch (Exception e) {
            return null;
        }
    }

}