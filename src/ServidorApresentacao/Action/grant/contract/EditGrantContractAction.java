/*
 * Created on 20/Dec/2003
 */

package ServidorApresentacao.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import Dominio.grant.contract.GrantContract;

/**
 * @author Barbosa
 * @author Pica
 *  
 */

public class EditGrantContractAction extends DispatchAction
{

    /*
	 * Fills the form with the correspondent data
	 */
    public ActionForward prepareEditGrantContractForm(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        request.getParameter("idGrantContract");
        request.getParameter("idGrantOwner");

        
        /*
		 * GrantContract grantContract = null; String idGrantContractString =
		 * request.getParameter("idGrantContract");
		 * 
		 * 
		 * if(idGrantContractString == null || idGrantContractString == "") { //Contracto novo
		 * grantContract = new GrantContract(); } else { //ler o contrato! Integer idGrantContract = new
		 * Integer(idGrantContractString);
		 *  
		 */

        //		Object[] args = { idInternal };
        //		IUserView userView = SessionUtils.getUserView(request);
        //		infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService(userView, "ReadGrantOwner",
		// args);
        //
        //		if (infoGrantOwner != null)
        //			request.setAttribute("infoGrantOwner", infoGrantOwner);
        //
        //		//Ler contractos
        //		List infoGrantContractList =
        //			(List) ServiceUtils.executeService(userView, "ReadAllContractsByGrantOwner", args);
        //
        //		if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
        //			request.setAttribute("infoGrantContractList", infoGrantContractList);
        //
        return mapping.findForward("edit-grant-contract");
    }
    /*
	 * Delete a grant contract
	 */
    public ActionForward deleteGrantContract(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {
        return null;
    }

}