/*
 * Created on 10 Mar 2004
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
public class ManageGrantContractRegimeAction extends FenixDispatchAction
{

	public ActionForward prepareManageGrantContractRegime(
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
				return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract-regime",null);
			}

			//Read Contract
			Object[] args = { idContract };
			IUserView userView = SessionUtils.getUserView(request);
			InfoGrantContract infoGrantContract =
				(InfoGrantContract) ServiceUtils.executeService(userView, "ReadGrantContract", args);

			request.setAttribute("idContract", idContract);
			request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());

			Object[] argsActiveContractRegime = { idContract, new Integer(1) };
			Object[] argsNotActiveContractRegime = { idContract, new Integer(0) };
			List infoGrantActiveContractRegimeList = (List) ServiceUtils.executeService(userView, "ReadGrantContractRegimeByContractAndState", argsActiveContractRegime);
			List infoGrantNotActiveContractRegimeList = (List) ServiceUtils.executeService(userView, "ReadGrantContractRegimeByContractAndState", argsNotActiveContractRegime);
						
			//If they exist put them on request
			if (infoGrantActiveContractRegimeList != null && !infoGrantActiveContractRegimeList.isEmpty())
			{
				request.setAttribute("infoGrantActiveContractRegimeList", infoGrantActiveContractRegimeList);
			}
			if (infoGrantNotActiveContractRegimeList != null && !infoGrantNotActiveContractRegimeList.isEmpty())
			{
				request.setAttribute("infoGrantNotActiveContractRegimeList", infoGrantNotActiveContractRegimeList);
			}
			
			//Presenting adittional information
			request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo().getGrantOwnerNumber());
			request.setAttribute("grantContractNumber", infoGrantContract.getContractNumber());
			
			return mapping.findForward("manage-grant-contract-regime");
		}
		catch (FenixServiceException e)
		{
			return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract-regime",null);
		}
		catch (Exception e)
		{
			return setError(request,mapping,"errors.grant.unrecoverable","manage-grant-contract-regime",null);
		}
	}
}