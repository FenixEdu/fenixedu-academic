/*
 * Created on 1/Ago/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlanAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = null;
        if (request.getParameter("degreeCurricularPlanId") != null) {
            degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
        }

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
        List curricularCourses = null;
        try {
            curricularCourses = (List) ServiceUtils.executeService(userView,
                    "ReadCurricularCoursesByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(curricularCourses, new BeanComparator("name"));

        List executionDegrees = null;
        try {
            executionDegrees = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(executionDegrees, new BeanComparator("infoExecutionYear.year"));

        request.setAttribute("curricularCoursesList", curricularCourses);
        request.setAttribute("executionDegreesList", executionDegrees);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        return mapping.findForward("viewDegreeCurricularPlan");
    }
}