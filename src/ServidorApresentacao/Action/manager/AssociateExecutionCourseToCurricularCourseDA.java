/*
 * Created on 9/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoExecutionPeriod;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class AssociateExecutionCourseToCurricularCourseDA extends FenixDispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));

        Object args[] = { executionPeriodId };

        List infoExecutionCoursesList = null;
        try
        {
            infoExecutionCoursesList =
                (List) ServiceUtils.executeService(
                    userView,
                    "ReadExecutionCoursesByExecutionPeriod",
                    args);

        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                e.getMessage(),
                mapping.findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        InfoExecutionPeriod infoExecutionPeriod = null;

        try
        {
            infoExecutionPeriod =
                (InfoExecutionPeriod) ServiceUtils.executeService(userView, "ReadExecutionPeriod", args);

        } catch (NonExistingServiceException e)
        {
            throw new NonExistingActionException(
                e.getMessage(),
                mapping.findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException);
        }
        String ExecutionPeriodNameAndYear =
            new String(
                infoExecutionPeriod.getName()
                    + "-"
                    + infoExecutionPeriod.getInfoExecutionYear().getYear());
        request.setAttribute("executionPeriodNameAndYear", ExecutionPeriodNameAndYear);
        request.setAttribute("name", "associate");
        request.setAttribute("infoExecutionCoursesList", infoExecutionCoursesList);

        return mapping.findForward("viewExecutionCoursesToAssociate");
    }

    public ActionForward associate(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm associateForm = (DynaActionForm) form;

        Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
        Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
        if (associateForm.get("executionCourseId") == null)
            return mapping.findForward("viewExecutionCoursesToAssociate");

        Integer executionCourseId = new Integer((String) associateForm.get("executionCourseId"));

        Object args[] = { executionCourseId, curricularCourseId, executionPeriodId };

        try
        {
            ServiceUtils.executeService(userView, "AssociateExecutionCourseToCurricularCourse", args);
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(
                e.getMessage(),
                mapping.findForward("readAvailableExecutionPeriods"));
        } catch (NonExistingServiceException ex)
        {
            if (ex.getMessage().equals("message.nonExistingCurricularCourse"))
                throw new NonExistingActionException(
                    ex.getMessage(),
                    mapping.findForward("readDegreeCurricularPlan"));
            else if (ex.getMessage().equals("message.nonExisting.executionCourse"))
                throw new NonExistingActionException(ex.getMessage(), "");
            else
                throw new NonExistingActionException(
                    ex.getMessage(),
                    mapping.findForward("readAvailableExecutionPeriods"));
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        return mapping.findForward("readCurricularCourse");
    }
}