/*
 * Created on 27/Mai/2003
 * 
 *  
 */
package ServidorApresentacao.Action.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.SiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.notAuthorizedServiceDeleteException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *  
 */
public class ExamEnrollmentManager extends FenixDispatchAction
{

    public ActionForward viewExamsToEnroll(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Object[] args = { userView.getUtilizador()};
        SiteView siteView = null;
        try
        {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadExamsByStudent", args);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
        if (request.getAttribute("alreadyEnrolledError") != null)
        {
            ActionErrors actionErrors = new ActionErrors();
            ActionError actionError =
                new ActionError((String) request.getAttribute("alreadyEnrolledError"));
            actionErrors.add("alreadyEnrolledError", actionError);
            saveErrors(request, actionErrors);
        }

        request.setAttribute("siteView", siteView);

        return mapping.findForward("sucess");
    }

    public ActionForward enrollStudent(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String examIdString = request.getParameter("objectCode");
        Integer examId = new Integer(examIdString);

        Object[] args = { userView.getUtilizador(), examId };

        try
        {

            ServiceUtils.executeService(userView, "EnrollStudentInExam", args);
        }
        catch (ExistingServiceException e)
        {
            request.setAttribute("alreadyEnrolledError", "error.alreadyEnrolledError");
        }
        catch (InvalidArgumentsServiceException e)
        {
            throw new FenixActionException(e);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return viewExamsToEnroll(mapping, form, request, response);
    }

    public ActionForward unEnrollStudent(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String examIdString = request.getParameter("objectCode");
        Integer examId = new Integer(examIdString);

        Object[] args = { userView.getUtilizador(), examId };

        try
        {

            ServiceUtils.executeService(userView, "UnEnrollStudentInExam", args);
        }
        catch (notAuthorizedServiceDeleteException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "notAuthorizedUnEnrollment",
                new ActionError("error.notAuthorizedUnEnrollment"));
            saveErrors(request, actionErrors);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return viewExamsToEnroll(mapping, form, request, response);
    }

}
