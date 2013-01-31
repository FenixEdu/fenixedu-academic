/*
 * Created on 15/Fev/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.grant.contract.GrantCostCenter;
import net.sourceforge.fenixedu.domain.grant.contract.GrantPaymentEntity;
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
		path = "/manageGrantCostCenter",
		input = "/manageGrantCostCenter.do?page=0&method=prepareManageGrantCostCenter",
		attribute = "voidForm",
		formBean = "voidForm",
		scope = "request",
		parameter = "method")
@Forwards(value = { @Forward(
		name = "manage-grant-costcenter",
		path = "/facultyAdmOffice/grant/contract/manageGrantCostCenter.jsp",
		tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.costcenter")) })
public class ManageGrantCostCenterAction extends FenixDispatchAction {
	public ActionForward prepareManageGrantCostCenter(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<GrantCostCenter> grantCenterList = new ArrayList<GrantCostCenter>();
		for (final GrantPaymentEntity grantPaymentEntity : rootDomainObject.getGrantPaymentEntitys()) {
			if (grantPaymentEntity.isCostCenter()) {
				grantCenterList.add((GrantCostCenter) grantPaymentEntity);
			}
		}
		request.setAttribute("grantCenterList", grantCenterList);
		return mapping.findForward("manage-grant-costcenter");
	}
}