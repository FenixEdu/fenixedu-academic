/*
 * Created on 15/Mai/2003
 *
 */
package ServidorApresentacao.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegree;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Filtro.exception.FenixFilterException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class ReadDegreeAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeId = null;
        if (request.getParameter("degreeId") != null) {
            degreeId = new Integer(request.getParameter("degreeId"));
        }
        Object args[] = { degreeId };

        InfoDegree degree = null;

        try {
            degree = (InfoDegree) ServiceUtils.executeService(userView, "ReadDegree", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegree", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        // in case the degree really exists
        List degreeCurricularPlans = null;

        try {
            degreeCurricularPlans = (List) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlansByDegree", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(degreeCurricularPlans);
        request.setAttribute("infoDegree", degree);
        request.setAttribute("curricularPlansList", degreeCurricularPlans);
        return mapping.findForward("viewDegree");
    }
}