/*
 * Created on 3/Jul/2004
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.grant.contract.InfoGrantContract;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantContractMovementAction extends FenixDispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareManageGrantContractMovement(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		try
		{
			Integer idContract = null;
			try
			{
				if (request.getAttribute("idContract") != null)
				{
					idContract = (Integer) request.getAttribute("idContract");
				}
				else
				{
					idContract = new Integer(request.getParameter("idContract"));
				}
			}
			catch (Exception e)
			{
				request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
				request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
				return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract-movement",null);
			}

			//Read Contract
			Object[] args = { idContract };
			IUserView userView = SessionUtils.getUserView(request);
			InfoGrantContract infoGrantContract =
				(InfoGrantContract) ServiceUtils.executeService(userView, "ReadGrantContract", args);

			request.setAttribute("idContract", idContract);
			request.setAttribute("idGrantOwner",infoGrantContract.getGrantOwnerInfo().getIdInternal());

			List infoGrantContractMovementsList =
				(List) ServiceUtils.executeService(userView, "ReadAllGrantMovementsByContract", args);

			if (infoGrantContractMovementsList != null && !infoGrantContractMovementsList.isEmpty())
				request.setAttribute("infoGrantContractMovementsList", infoGrantContractMovementsList);

			//Presenting adittional information
			request.setAttribute("contractNumber", infoGrantContract.getContractNumber());
			request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo().getGrantOwnerNumber());
		}
		catch (FenixServiceException e)
		{
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-contract-movement", null);
		}
		return mapping.findForward("manage-grant-contract-movement");
	}
}