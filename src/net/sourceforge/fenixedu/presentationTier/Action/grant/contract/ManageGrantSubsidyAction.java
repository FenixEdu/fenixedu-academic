/*
 * Created on 04 Mar 2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadAllGrantSubsidiesByGrantContractAndState;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantContract;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Barbosa
 * @author Pica
 */

@Mapping(
		module = "facultyAdmOffice",
		path = "/manageGrantSubsidy",
		input = "/manageGrantSubsidy.do?page=0&method=prepareManageGrantSubsidyForm",
		attribute = "voidForm",
		formBean = "voidForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(
		name = "manage-grant-subsidy",
		path = "/facultyAdmOffice/grant/contract/manageGrantSubsidy.jsp",
		tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.costcenter")) })
public class ManageGrantSubsidyAction extends FenixDispatchAction {

	public ActionForward prepareManageGrantSubsidyForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

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
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-subsidy", null);
		}

		// Read Contract
		Object[] args = { idContract };
		IUserView userView = UserView.getUser();
		InfoGrantContract infoGrantContract = (InfoGrantContract) ServiceUtils.executeService("ReadGrantContract", args);

		request.setAttribute("idContract", idContract);
		request.setAttribute("idGrantOwner", infoGrantContract.getGrantOwnerInfo().getIdInternal());

		// Read Subsidies

		List infoGrantActiveSubsidyList =
				ReadAllGrantSubsidiesByGrantContractAndState.run(idContract, InfoGrantSubsidy.getActiveStateValue());
		List infoGrantNotActiveSubsidyList =
				ReadAllGrantSubsidiesByGrantContractAndState.run(idContract, InfoGrantSubsidy.getInactiveStateValue());

		// If they exist put them on request
		if (infoGrantActiveSubsidyList != null && !infoGrantActiveSubsidyList.isEmpty()) {
			request.setAttribute("infoGrantActiveSubsidyList", infoGrantActiveSubsidyList);
		}
		if (infoGrantNotActiveSubsidyList != null && !infoGrantNotActiveSubsidyList.isEmpty()) {
			request.setAttribute("infoGrantNotActiveSubsidyList", infoGrantNotActiveSubsidyList);
		}

		// Presenting adittional information
		request.setAttribute("grantOwnerNumber", infoGrantContract.getGrantOwnerInfo().getGrantOwnerNumber());
		request.setAttribute("grantContractNumber", infoGrantContract.getContractNumber());

		return mapping.findForward("manage-grant-subsidy");

	}
}