/*
 * Created on 10 Mar 2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContractRegime;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Barbosa
 * @author Pica
 */
public class ManageGrantContractRegimeAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantContractRegime(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {

	Integer idContract = null;
	try {
	    if (request.getAttribute("idContract") != null) {
		idContract = (Integer) request.getAttribute("idContract");
	    } else {
		idContract = new Integer(request.getParameter("idContract"));
	    }
	} catch (Exception e) {
	    request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
	    request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
	    return setError(request, mapping, "errors.grant.unrecoverable",
		    "manage-grant-contract-regime", null);
	}

	//Read Contract
	Object[] args = { idContract };
	IUserView userView = UserView.getUser();
	InfoGrantContract infoGrantContract = (InfoGrantContract) ServiceUtils.executeService(
		"ReadGrantContract", args);

	request.setAttribute("idContract", idContract);
	request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());

	Object[] argsActiveContractRegime = { idContract, InfoGrantContractRegime.getActiveState() };
	Object[] argsNotActiveContractRegime = { idContract, InfoGrantContractRegime.getInactiveState() };
	List infoGrantActiveContractRegimeList = (List) ServiceUtils.executeService(
		"ReadGrantContractRegimeByContractAndState", argsActiveContractRegime);
	List infoGrantNotActiveContractRegimeList = (List) ServiceUtils.executeService(
		"ReadGrantContractRegimeByContractAndState", argsNotActiveContractRegime);

	//If they exist put them on request
	if (infoGrantActiveContractRegimeList != null && !infoGrantActiveContractRegimeList.isEmpty()) {
	    request.setAttribute("infoGrantActiveContractRegimeList", infoGrantActiveContractRegimeList);
	}
	if (infoGrantNotActiveContractRegimeList != null
		&& !infoGrantNotActiveContractRegimeList.isEmpty()) {
	    request.setAttribute("infoGrantNotActiveContractRegimeList",
		    infoGrantNotActiveContractRegimeList);
	}

	//Presenting adittional information
	request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo()
		.getGrantOwnerNumber());
	request.setAttribute("grantContractNumber", infoGrantContract.getContractNumber());

	return mapping.findForward("manage-grant-contract-regime");

    }
}