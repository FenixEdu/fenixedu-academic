/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
		path = "/editGrantType",
		input = "/editGrantType.do?page=0&method=prepareEditGrantTypeForm",
		attribute = "editGrantTypeForm",
		formBean = "editGrantTypeForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(name = "manage-grant-type", path = "/manageGrantType.do?method=prepareManageGrantTypeForm"),
		@Forward(name = "edit-grant-type", path = "/facultyAdmOffice/grant/contract/editGrantType.jsp", tileProperties = @Tile(
				title = "private.teachingstaffandresearcher.miscellaneousmanagement.typesofscholarship")) })
public class EditGrantTypeAction extends FenixDispatchAction {

	public ActionForward prepareEditGrantTypeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Integer idGrantType = getIntegerFromRequest(request, "idGrantType");
		if (idGrantType != null) {
			GrantType grantType = rootDomainObject.readGrantTypeByOID(idGrantType);
			request.setAttribute("grantType", grantType);
		}
		return mapping.findForward("edit-grant-type");
	}

}