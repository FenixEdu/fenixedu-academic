package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.candidate.ReadExecutionDegreeByDegreeCurricularPlanID;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "coordinator", path = "/prepareCandidateApproval", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "ExecutionDegreeChosen",
        path = "/displayListToSelectCandidates.do?method=prepareSelectCandidates", tileProperties = @Tile(title = "teste81")) })
public class PrepareCandidateApprovalDispatchAction extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        CoordinatedDegreeInfo.setCoordinatorContext(request);
        return super.execute(mapping, actionForm, request, response);
    }

    public ActionForward chooseExecutionDegree(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        InfoExecutionDegree infoExecutionDegree;
        String degreeCurricularPlanID = null;
        if (request.getParameter("degreeCurricularPlanID") != null) {
            degreeCurricularPlanID = request.getParameter("degreeCurricularPlanID");
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        }

        try {
            infoExecutionDegree =
                    ReadExecutionDegreeByDegreeCurricularPlanID.runReadExecutionDegreeByDegreeCurricularPlanID(
                            degreeCurricularPlanID, new Integer(1));
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException();
        }

        request.setAttribute("degree", infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getSigla());
        request.setAttribute("executionYear", infoExecutionDegree.getInfoExecutionYear().getYear());

        request.setAttribute("executionDegreeID", infoExecutionDegree.getExternalId());

        return mapping.findForward("ExecutionDegreeChosen");
    }

}