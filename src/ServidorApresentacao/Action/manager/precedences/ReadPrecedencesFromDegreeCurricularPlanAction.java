package ServidorApresentacao.Action.manager.precedences;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author David Santos in Jul 28, 2004
 */

public class ReadPrecedencesFromDegreeCurricularPlanAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = {degreeCurricularPlanID};
        
        try {
			Map result = (Map) ServiceUtils.executeService(userView, "ReadPrecedencesFromDegreeCurricularPlan", args);
			request.setAttribute("precedences", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

		request.setAttribute("degreeId", degreeID);
		request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

		return mapping.findForward("showPrecedences");
    }
}