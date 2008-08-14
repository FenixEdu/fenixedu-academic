/*
 * Created on 10/Dec/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.grant.owner.InfoGrantOwner;
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
public class ManageGrantContractAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantContractForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	Integer idInternal = null;
	if (request.getParameter("idInternal") != null) {
	    idInternal = new Integer(request.getParameter("idInternal"));
	} else if ((Integer) request.getAttribute("idInternal") != null) {
	    idInternal = (Integer) request.getAttribute("idInternal");
	}

	// Run the service
	Object[] args = { idInternal };
	IUserView userView = UserView.getUser();
	List infoGrantContractList = (List) ServiceUtils.executeService("ReadAllContractsByGrantOwner", args);

	if (infoGrantContractList != null && !infoGrantContractList.isEmpty())
	    request.setAttribute("infoGrantContractList", infoGrantContractList);

	// Needed for return to manage contracts
	request.setAttribute("idInternal", idInternal);

	InfoGrantOwner infoGrantOwner = (InfoGrantOwner) ServiceUtils.executeService("ReadGrantOwner", args);
	request.setAttribute("grantOwnerNumber", infoGrantOwner.getGrantOwnerNumber());
	request.setAttribute("grantOwnerName", infoGrantOwner.getPersonInfo().getNome());

	return mapping.findForward("manage-grant-contract");
    }
}