/*
 * Created on Jul 27, 2004
 *
 */
package ServidorApresentacao.Action.manager.curricularCourseGroupManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author João Mota
 *
 */
public class CurricularCouseGroupManagementDispatchAction extends FenixDispatchAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        // in case the degreeCurricularPlan really exists
        List curricularCourseGroups = null;
        try {
            curricularCourseGroups = (List) ServiceUtils.executeService(userView,
                    "ReadCurricularCourseGroupsByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(curricularCourseGroups, new BeanComparator("name"));

        List executionDegrees = null;
        try {
            executionDegrees = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(executionDegrees, new BeanComparator("infoExecutionYear.year"));

        request.setAttribute("curricularCourseGroups", curricularCourseGroups);
        request.setAttribute("executionDegreesList", executionDegrees);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        return mapping.findForward("viewGroups");
    }
}
