package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowStudyPlanForCandidateAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = getUserView(request);
        ArrayList candidateStudyPlan = null;

        // transport candidate ID
        request.setAttribute("candidateID", request.getParameter("candidateID"));
        
        InfoMasterDegreeCandidate infoMasterDegreeCandidate = getMasterDegreeCandidate(userView, session);

        if (infoMasterDegreeCandidate != null) {
            candidateStudyPlan = getCandidateStudyPlanByCandidateID(infoMasterDegreeCandidate
                    .getIdInternal(), userView);

            request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
            request.setAttribute("candidateStudyPlan", candidateStudyPlan);
        }

        return mapping.findForward("Sucess");
    }

    private InfoMasterDegreeCandidate getMasterDegreeCandidate(IUserView userView, HttpSession session) {
        if (session != null) {

            Object args[] = { userView.getUtilizador() };
            List candidates = null;
            try {
                candidates = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadPersonCandidates", args);
            } catch (Exception e) {
                return null;
            }

            if (candidates.size() == 1) {
                session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, candidates.get(0));
                return (InfoMasterDegreeCandidate) candidates.get(0);
            }

            session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
            return (InfoMasterDegreeCandidate) candidates.get(0);
        }
        return null;
    }

    private ArrayList getCandidateStudyPlanByCandidateID(Integer candidateID, IUserView userView) {
        Object[] args = { candidateID };

        try {
            return (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCandidateEnrolmentsByCandidateID", args);
        } catch (Exception e) {
            return null;
        }
    }

}