/*
 * Created on 20/Jan/2004
 */

package net.sourceforge.fenixedu.presentationTier.Action.grant.contract;

import java.util.ArrayList;
import java.util.List;

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
@Mapping(module = "facultyAdmOffice", path = "/manageGrantType",
        input = "/manageGrantType.do?page=0&method=prepareManageGrantTypeForm", attribute = "voidForm", formBean = "voidForm",
        scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "manage-grant-type", path = "/facultyAdmOffice/grant/contract/manageGrantType.jsp",
        tileProperties = @Tile(title = "private.teachingstaffandresearcher.miscellaneousmanagement.typesofscholarship")) })
public class ManageGrantTypeAction extends FenixDispatchAction {

    public ActionForward prepareManageGrantTypeForm(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        List<GrantType> grantTypes = new ArrayList<GrantType>(rootDomainObject.getGrantTypes());
        request.setAttribute("grantTypes", grantTypes);
        return mapping.findForward("manage-grant-type");

    }
}