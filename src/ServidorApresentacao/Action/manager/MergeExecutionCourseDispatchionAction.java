/*
 * Created on 3/Dez/2003
 *  
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a> 3/Dez/2003
 *  
 */
public class MergeExecutionCourseDispatchionAction extends DispatchAction
{

    public ActionForward chooseDegreesAndExecutionPeriod(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm degreesForm = (DynaActionForm) form;
        Integer sourceDegreeId = (Integer) degreesForm.get("sourceDegreeId");
        Integer destinationDegreeId = (Integer) degreesForm.get("destinationDegreeId");
        Integer executionPeriodId = (Integer) degreesForm.get("executionPeriodId");
        Object[] args1 = { destinationDegreeId, executionPeriodId };
        Object[] args2 = { sourceDegreeId, executionPeriodId };
        try
        {
            List destinationExecutionCourses =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionCoursesByDegreeAndExecutionPeriodId",
                    args1);
            List sourceExecutionCourses =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionCoursesByDegreeAndExecutionPeriodId",
                    args2);
            request.setAttribute("sourceExecutionCourses", sourceExecutionCourses);
            request.setAttribute("destinationExecutionCourses", destinationExecutionCourses);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("chooseExecutionCourses");
    }

    public ActionForward prepareChooseDegreesAndExecutionPeriod(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = {
        };

        try
        {
            List degrees = (List) ServiceUtils.executeService(userView, "ReadDegrees", args);
            List executionPeriods =
                (List) ServiceUtils.executeService(userView, "ReadAllExecutionPeriods", args);

            request.setAttribute("sourceDegrees", degrees);
            request.setAttribute("destinationDegrees", degrees);
            request.setAttribute("executionPeriods", executionPeriods);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("chooseDegreesAndExecutionPeriod");
    }

    public ActionForward mergeExecutionCourses(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm mergeExecutionCoursesForm = (DynaActionForm) form;
        Integer sourceExecutionCourseId =
            (Integer) mergeExecutionCoursesForm.get("sourceExecutionCourseId");
        Integer destinationExecutionCourseId =
            (Integer) mergeExecutionCoursesForm.get("destinationExecutionCourseId");
        Object[] args = { destinationExecutionCourseId, sourceExecutionCourseId };

        try
        {
            ServiceUtils.executeService(userView, "MergeExecutionCourses", args);

        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("sucess");
    }
}
