/*
 * Created on 20/Jan/2004
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
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
@Mapping(module = "facultyAdmOffice", path = "/editGrantCostCenter", input = "/editGrantCostCenter.do?page=0&method=prepareEditGrantCostCenterForm", attribute = "editGrantCostCenterForm", formBean = "editGrantCostCenterForm", scope = "request", parameter = "method")
@Forwards(value = {
	@Forward(name = "edit-grant-costcenter", path = "/facultyAdmOffice/grant/contract/editGrantCostCenter.jsp", tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.costcenter")),
	@Forward(name = "manage-grant-costcenter", path = "/manageGrantCostCenter.do?method=prepareManageGrantCostCenter") })
public class EditGrantCostCenterAction extends FenixDispatchAction {

    public ActionForward prepareEditGrantCostCenterForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Integer idGrantCostCenter = getIntegerFromRequest(request, "idGrantCostCenter");
	if (idGrantCostCenter != null) {
	    GrantCostCenter grantCostCenter = (GrantCostCenter) rootDomainObject.readGrantPaymentEntityByOID(idGrantCostCenter);
	    request.setAttribute("grantCostCenter", grantCostCenter);
	}
	return mapping.findForward("edit-grant-costcenter");
    }
}