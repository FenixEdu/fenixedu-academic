/*
 * Created on 15/Mai/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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