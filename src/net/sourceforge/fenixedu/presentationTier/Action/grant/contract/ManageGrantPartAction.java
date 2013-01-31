/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadAllGrantPartsByGrantSubsidy;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.contract.ReadGrantSubsidy;
import net.sourceforge.fenixedu.dataTransferObject.grant.contract.InfoGrantSubsidy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

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
		path = "/manageGrantPart",
		input = "/manageGrantPart.do?page=0&method=prepareManageGrantPart",
		attribute = "voidForm",
		formBean = "voidForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(
		name = "manage-grant-part",
		path = "/facultyAdmOffice/grant/contract/manageGrantPart.jsp",
		tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.costcenter")) })
public class ManageGrantPartAction extends FenixDispatchAction {
	/*
	 * Fills the form with the correspondent data
	 */
	public ActionForward prepareManageGrantPart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Integer idSubsidy = null;
		try {
			if (request.getAttribute("idSubsidy") != null) {
				idSubsidy = (Integer) request.getAttribute("idSubsidy");
			} else {
				idSubsidy = new Integer(request.getParameter("idSubsidy"));
			}
		} catch (Exception e) {
			request.setAttribute("idContract", new Integer(request.getParameter("idContract")));
			request.setAttribute("idGrantOwner", new Integer(request.getParameter("idGrantOwner")));
			return setError(request, mapping, "errors.grant.unrecoverable", "manage-grant-part", null);
		}

		// Read Subsidy

		IUserView userView = UserView.getUser();
		InfoGrantSubsidy infoGrantSubsidy = (InfoGrantSubsidy) ReadGrantSubsidy.run(idSubsidy);

		request.setAttribute("idSubsidy", idSubsidy);
		request.setAttribute("idContract", infoGrantSubsidy.getInfoGrantContract().getIdInternal());
		request.setAttribute("idGrantOwner", infoGrantSubsidy.getInfoGrantContract().getGrantOwnerInfo().getIdInternal());

		List infoGrantPartList = ReadAllGrantPartsByGrantSubsidy.run(idSubsidy);

		if (infoGrantPartList != null && !infoGrantPartList.isEmpty()) {
			request.setAttribute("infoGrantPartList", infoGrantPartList);
		}

		// Presenting adittional information
		request.setAttribute("subsidyValue", infoGrantSubsidy.getValue());
		request.setAttribute("subsidyTotalCost", infoGrantSubsidy.getTotalCost());

		return mapping.findForward("manage-grant-part");
	}
}