/*
 * Created on 20/Jan/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.grant.contract.InfoGrantSubsidy;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantPartAction extends DispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareManageGrantPart(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			Integer idSubsidy = null;
			try
			{
				if (request.getAttribute("idSubsidy") != null)
					idSubsidy = (Integer) request.getAttribute("idSubsidy");
				else
					idSubsidy = new Integer(request.getParameter("idSubsidy"));
			}
			catch (Exception e)
			{
				request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
				request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
				return setError(
					request,
					mapping,
					"errors.grant.unrecoverable",
					"manage-grant-part",
					null);
			}

			Object[] args = { idSubsidy };
			IUserView userView = SessionUtils.getUserView(request);
			InfoGrantSubsidy infoGrantSubsidy =
				(InfoGrantSubsidy) ServiceUtils.executeService(userView, "ReadGrantSubsidy", args);

			request.setAttribute("idContract", infoGrantSubsidy.getInfoGrantContract().getIdInternal());
			request.setAttribute(
				"idGrantOwner",
				infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo().getIdInternal());
			request.setAttribute("idSubsidy", idSubsidy);

			List infoGrantPartList =
				(List) ServiceUtils.executeService(userView, "ReadAllGrantPartsByGrantSubsidy", args);

			//If they exist put them on request
			if (infoGrantPartList != null && !infoGrantPartList.isEmpty())
				request.setAttribute("infoGrantPartList", infoGrantPartList);

			//Presenting adittional information
			request.setAttribute("subsidyValue", infoGrantSubsidy.getValue());
			request.setAttribute("subsidyTotalCost", infoGrantSubsidy.getTotalCost());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-part", null);
		}
		return mapping.findForward("manage-grant-part");
	}

	/*
	 * Sets an error to be displayed in the page and sets the mapping forward
	 */
	private ActionForward setError(
		HttpServletRequest request,
		ActionMapping mapping,
		String errorMessage,
		String forwardPage,
		Object actionArg)
	{
		ActionErrors errors = new ActionErrors();
		ActionError error = new ActionError(errorMessage, actionArg);
		errors.add(errorMessage, error);
		saveErrors(request, errors);

		if (forwardPage != null)
			return mapping.findForward(forwardPage);
		else
			return mapping.getInputForward();
	}
}