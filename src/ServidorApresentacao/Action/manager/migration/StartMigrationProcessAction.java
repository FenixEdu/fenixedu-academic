package ServidorApresentacao.Action.manager.migration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author David Santos
 * Jan 26, 2004
 */

public class StartMigrationProcessAction extends Action
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = (IUserView) request.getSession().getAttribute(SessionConstants.U_VIEW);
		String password = request.getParameter("password");
		String method = request.getParameter("method");
		String curriculum = request.getParameter("curriculum");
		Object args[] = {password, method, curriculum};
		
		try
		{
			ServiceUtils.executeService(userView, "ClearCacheForMigrationProcess", null);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		
		Runtime.getRuntime().gc();

		try
		{
			ServiceUtils.executeService(userView, "StartMigrationProcess", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		return mapping.findForward("success");
	}
}