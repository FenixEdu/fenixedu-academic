package ServidorApresentacao.Action.manager.precedences;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author David Santos in Jul 28, 2004
 */

public class ReadPrecedencesFromDegreeCurricularPlanAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanID = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanID };

        try {
            Map result = (Map) ServiceManagerServiceFactory.executeService(userView,
                    "ReadPrecedencesFromDegreeCurricularPlan", args);
            request.setAttribute("precedences", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("degreeId", degreeID);
        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanID);

        return mapping.findForward("showPrecedences");
    }
}