package ServidorApresentacao.Action.masterDegree.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

public class PrepareCandidateApprovalDispatchAction extends DispatchAction {

    public ActionForward chooseExecutionDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionDegree infoExecutionDegree;
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));

        Object args[] = { degreeCurricularPlanID, new Integer(1) };

        try {
            infoExecutionDegree = (InfoExecutionDegreeWithInfoExecutionYearAndDegreeCurricularPlan) ServiceUtils
                    .executeService(userView, "ReadExecutionDegreeByDegreeCurricularPlanID", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException();
        }

        request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                .getSigla());
        request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());

        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        
        request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal());
        
        return mapping.findForward("ExecutionDegreeChosen");
    }

}