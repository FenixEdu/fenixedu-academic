package ServidorApresentacao.Action.masterDegree.coordinator;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

public class CandidateOperationDispatchAction extends DispatchAction {

    public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanID"));

            List candidates = null;
            Object args[] = { degreeCurricularPlanId };

            try {
                candidates = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadDegreeCandidates", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (candidates.size() == 0)
                throw new NonExistingActionException("error.exception.nonExistingCandidates", "", null);

            request.setAttribute("masterDegreeCandidateList", candidates);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            return mapping.findForward("ViewList");
        }
        throw new Exception();
    }

    public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        Integer candidateID = Integer.valueOf(request.getParameter("candidateID"));

        Object[] args = { candidateID };

        InfoMasterDegreeCandidate infoMasterDegreeCandidate;
        try {
            infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "ReadMasterDegreeCandidateByID", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException();
        }
        
        request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);

        return mapping.findForward("ActionReady");
    }

}