/*
 * Created on 10/Dec/2003
 */

package ServidorApresentacao.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.grant.owner.InfoGrantOwner;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author Barbosa
 * @author Pica
 */

public class ManageGrantContractAction extends DispatchAction
{
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareManageGrantContractForm(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception
	{
		Integer idInternal = null;

		//TODO.. verificar so que null.. ou tb verificar se são strings vazias!!!
		if (request.getParameter("idInternal") != null)
			idInternal = new Integer(request.getParameter("idInternal"));
		else if ((Integer) request.getAttribute("idInternal") != null)
			idInternal = (Integer) request.getAttribute("idInternal");
		

		//Run the service
		Object[] args = { idInternal };
		IUserView userView = SessionUtils.getUserView(request);
		List infoGrantContractList =
			(List) ServiceUtils.executeService(userView, "ReadAllContractsByGrantOwner", args);
        
		//If they exist put them on request
		if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
			request.setAttribute("infoGrantContractList", infoGrantContractList);

        //Needed for return to manage contracts
        request.setAttribute("idInternal", idInternal);
        
        //Read grant owner to present the information in a display window
        InfoGrantOwner infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService(userView, "ReadGrantOwner", args);
        request.setAttribute("grantOwnerNumber", infoGrantOwner.getGrantOwnerNumber());
        request.setAttribute("grantOwnerName", infoGrantOwner.getPersonInfo().getNome());

		return mapping.findForward("manage-grant-contract");
	}
}