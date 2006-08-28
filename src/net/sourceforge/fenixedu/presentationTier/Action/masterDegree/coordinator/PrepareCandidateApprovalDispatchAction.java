package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class PrepareCandidateApprovalDispatchAction extends FenixDispatchAction {

    public ActionForward chooseExecutionDegree(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);

        InfoExecutionDegree infoExecutionDegree;
        Integer degreeCurricularPlanID = null;
        if(request.getParameter("degreeCurricularPlanID") != null){
            degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanID"));
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }
        
        Object args[] = { degreeCurricularPlanID, new Integer(1) };

        try {
            infoExecutionDegree = (InfoExecutionDegree) ServiceUtils
                    .executeService(userView, "ReadExecutionDegreeByDegreeCurricularPlanID", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException();
        }

        request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                .getSigla());
        request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());
        
        request.setAttribute("executionDegreeID", infoExecutionDegree.getIdInternal());
        
        return mapping.findForward("ExecutionDegreeChosen");
    }

}