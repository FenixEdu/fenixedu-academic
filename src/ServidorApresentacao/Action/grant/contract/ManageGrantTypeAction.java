/*
 * Created on 20/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantTypeAction extends FenixDispatchAction
{

	public ActionForward prepareManageGrantTypeForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			Object[] args = {};
			IUserView userView = SessionUtils.getUserView(request);
			List infoGrantTypeList =
				(List) ServiceUtils.executeService(userView, "ReadAllGrantTypes", args);

			if (infoGrantTypeList != null && !infoGrantTypeList.isEmpty())
				request.setAttribute("infoGrantTypeList", infoGrantTypeList);
			
			return mapping.findForward("manage-grant-type");
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-type", null);
		}
	}
}