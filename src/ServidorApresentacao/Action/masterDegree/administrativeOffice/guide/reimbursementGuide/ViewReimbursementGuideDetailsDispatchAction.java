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

import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class ViewReimbursementGuideDetailsDispatchAction extends FenixDispatchAction {

    public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer reimbursementGuideId = new Integer(this.getFromRequest("id", request));

        InfoReimbursementGuide infoReimbursementGuide = null;

        Object args[] = { reimbursementGuideId };
        try {
            infoReimbursementGuide = (InfoReimbursementGuide) ServiceUtils.executeService(userView,
                    "ViewReimbursementGuide", args);

            request.setAttribute(SessionConstants.REIMBURSEMENT_GUIDE, infoReimbursementGuide);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
        }

        return mapping.findForward("start");

    }

    private String getFromRequest(String parameter, HttpServletRequest request) {
        String parameterString = request.getParameter(parameter);
        if (parameterString == null) {
            parameterString = (String) request.getAttribute(parameter);
        }
        return parameterString;
    }

}