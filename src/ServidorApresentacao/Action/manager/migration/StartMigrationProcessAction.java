package ServidorApresentacao.Action.manager.migration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import middleware.studentMigration.enrollments.RunMigrationProcess;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorApresentacao.Action.exceptions.FenixActionException;

/**
 * @author David Santos
 * Jan 26, 2004
 */

public class StartMigrationProcessAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException
	{
		String password = request.getParameter("password");
		String method = request.getParameter("method");
		String curriculum = request.getParameter("curriculum");
		String args[] = { curriculum, method, password };

		try
		{
			RunMigrationProcess.main(args);
		} catch (Throwable e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("success");
	}
}