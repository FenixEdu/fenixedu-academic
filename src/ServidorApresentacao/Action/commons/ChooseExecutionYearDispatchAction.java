package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends DispatchAction
{

    public ActionForward prepareChooseExecutionYear(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);
        MessageResources messages = getResources(request);

        // Get Execution Year List

        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        GestorServicos serviceManager = GestorServicos.manager();
        ArrayList executionYearList = null;
        try
        {
            executionYearList =
                (ArrayList) serviceManager.executar(userView, "ReadExecutionYears", null);
        } catch (ExistingServiceException e)
        {
            throw new ExistingActionException(e);
        }

        if (request.getParameter("jspTitle") != null)
        {
            request.setAttribute("jspTitle", messages.getMessage(request.getParameter("jspTitle")));
        }

        Collections.sort(executionYearList, new BeanComparator("label"));
        request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearList);

        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward chooseExecutionYear(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
            request.setAttribute("executionYear", request.getParameter("executionYear"));

            request.setAttribute("jspTitle", request.getParameter("jspTitle"));

            return mapping.findForward("ChooseSuccess");
        } else
            throw new Exception();
    }
}