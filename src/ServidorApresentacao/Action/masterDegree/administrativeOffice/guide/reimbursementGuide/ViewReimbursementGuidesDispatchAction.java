/*
 * Created on 22/Abr/2004
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoGuide;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.InvalidGuideSituationActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.SituationOfGuide;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ViewReimbursementGuidesDispatchAction extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer guideNumber = new Integer(this.getFromRequest("number", request));
        Integer guideYear = new Integer(this.getFromRequest("year", request));
        Integer guideVersion = new Integer(this.getFromRequest("version", request));

        InfoGuide infoGuide = null;

        Object args[] = { guideNumber, guideYear, guideVersion };
        try {
            infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "ChooseGuide", args);

            request.setAttribute(SessionConstants.REIMBURSEMENT_GUIDES_LIST, infoGuide
                    .getInfoReimbursementGuides());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        if (infoGuide.getInfoReimbursementGuides().isEmpty() == true)
            throw new NonExistingActionException(
                    "error.exception.masterDegree.nonExistingReimbursementGuides", mapping
                            .findForward("error"));

        if (infoGuide.getInfoGuideSituation().getSituation().equals(SituationOfGuide.PAYED_TYPE)) {
            return mapping.findForward("start");
        }

        throw new InvalidGuideSituationActionException(mapping.findForward("error"));

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}