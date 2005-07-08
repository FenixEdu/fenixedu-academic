/*
 * Created on 9/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author lmac1
 */

public class AssociateExecutionCourseToCurricularCourseDA extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));

        Object args[] = { executionPeriodId };

        List infoExecutionCoursesList = null;
        try {
            infoExecutionCoursesList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByExecutionPeriod", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException(e.getMessage(), mapping
                    .findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        Collections.sort(infoExecutionCoursesList, new BeanComparator("nome"));

        InfoExecutionPeriod infoExecutionPeriod = ((InfoExecutionCourse) infoExecutionCoursesList.get(0))
                .getInfoExecutionPeriod();

        String ExecutionPeriodNameAndYear = new String(infoExecutionPeriod.getName() + "-"
                + infoExecutionPeriod.getInfoExecutionYear().getYear());
        request.setAttribute("executionPeriodNameAndYear", ExecutionPeriodNameAndYear);
        request.setAttribute("name", "associate");
        request.setAttribute("infoExecutionCoursesList", infoExecutionCoursesList);

        return mapping.findForward("viewExecutionCoursesToAssociate");
    }

    public ActionForward associate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm associateForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
        Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
        if (associateForm.get("executionCourseId") == null)
            return mapping.findForward("viewExecutionCoursesToAssociate");

        Integer executionCourseId = new Integer((String) associateForm.get("executionCourseId"));

        Object args[] = { executionCourseId, curricularCourseId, executionPeriodId };

        try {
            ServiceUtils.executeService(userView, "AssociateExecutionCourseToCurricularCourse", args);
        } catch (ExistingServiceException e) {
            throw new ExistingActionException(e.getMessage(), mapping
                    .findForward("readAvailableExecutionPeriods"));
        } catch (NonExistingServiceException ex) {
            if (ex.getMessage().equals("message.nonExistingCurricularCourse"))
                throw new NonExistingActionException(ex.getMessage(), mapping
                        .findForward("readDegreeCurricularPlan"));
            else if (ex.getMessage().equals("message.nonExisting.executionCourse"))
                throw new NonExistingActionException(ex.getMessage(), "");
            else
                throw new NonExistingActionException(ex.getMessage(), mapping
                        .findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException);
        }

        return mapping.findForward("readCurricularCourse");
    }
}