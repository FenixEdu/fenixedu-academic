/*
 * Created on 24/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author lmac1
 */
public class ReadExecutionCoursesAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
        List executionCourses = null;
        Object args[] = { executionPeriodId };
        try {
            executionCourses = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByExecutionPeriod", args);

            if (executionCourses != null && executionCourses.size() > 0) {
                Collections.sort(executionCourses, new BeanComparator("nome"));
                InfoExecutionPeriod infoExecutionPeriod = ((InfoExecutionCourse) executionCourses.get(0))
                        .getInfoExecutionPeriod();

                String executionPeriodNameAndYear = new String(infoExecutionPeriod.getName() + "-"
                        + infoExecutionPeriod.getInfoExecutionYear().getYear());
                request.setAttribute("executionPeriodNameAndYear", executionPeriodNameAndYear);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoExecutionCoursesList", executionCourses);

        //		InfoExecutionPeriod infoExecutionPeriod = null;
        //		
        //		try
        //		{
        //			infoExecutionPeriod =
        //				(InfoExecutionPeriod) ServiceUtils.executeService(
        //					userView,
        //					"ReadExecutionPeriodForExecutionCourseAssociation",
        //					args);
        //
        //		}
        //		catch (NonExistingServiceException e)
        //		{
        //			throw new NonExistingActionException(
        //				e.getMessage(),
        //				mapping.findForward("readAvailableExecutionPeriods"));
        //		}
        //		catch (FenixServiceException fenixServiceException)
        //		{
        //			throw new FenixActionException(fenixServiceException);
        //		}
        //		String ExecutionPeriodNameAndYear =
        //			new String(
        //				infoExecutionPeriod.getName()
        //					+ "-"
        //					+ infoExecutionPeriod.getInfoExecutionYear().getYear());

        return mapping.findForward("readExecutionCourses");
    }
}