package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate.ReadPersonCandidates;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadCandidateEnrolmentsByCandidateID;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowStudyPlanForCandidateAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        // transport candidate ID
        request.setAttribute("candidateID", request.getParameter("candidateID"));

        User userView = getUserView(request);
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = getMasterDegreeCandidate(userView, request);
        if (infoMasterDegreeCandidate != null) {
            request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
            request.setAttribute("candidateStudyPlan",
                    getCandidateStudyPlanByCandidateID(infoMasterDegreeCandidate.getExternalId(), userView));
        }

        return mapping.findForward("Sucess");
    }

    private InfoMasterDegreeCandidate getMasterDegreeCandidate(User userView, HttpServletRequest request) {
        List candidates = null;
        try {
            candidates = ReadPersonCandidates.run(userView.getUsername());
        } catch (Exception e) {
            return null;
        }

        if (candidates.size() == 1) {
            request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE, candidates.iterator().next());
        } else {
            request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
        }

        return (InfoMasterDegreeCandidate) candidates.iterator().next();
    }

    private ArrayList getCandidateStudyPlanByCandidateID(String candidateID, User userView) {
        try {
            return (ArrayList) ReadCandidateEnrolmentsByCandidateID.runReadCandidateEnrolmentsByCandidateID(candidateID);
        } catch (Exception e) {
            return null;
        }
    }

}