/*
 * Created on 30/Set/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author lmac1
 */

public class DeleteExecutionCoursesAction extends FenixAction
{

    public ActionForward execute(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm deleteForm = (DynaActionForm) form;

        List internalIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        Object args[] = { internalIds };

        List errorCodes = new ArrayList();

        try
        {
            errorCodes = (List) ServiceUtils.executeService(userView, "DeleteExecutionCourses", args);
        } catch (FenixServiceException fenixServiceException)
        {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        if (!errorCodes.isEmpty())
        {
            ActionErrors actionErrors = new ActionErrors();
            Iterator codesIter = errorCodes.iterator();
            ActionError error = null;
            while (codesIter.hasNext())
            {
                error =
                    new ActionError(
                        "errors.invalid.delete.not.empty.execution.course",
                        codesIter.next());
                actionErrors.add("errors.invalid.delete.not.empty.execution.course", error);
            }
            saveErrors(request, actionErrors);
        }
        return mapping.findForward("readExecutionCourses");
    }
}