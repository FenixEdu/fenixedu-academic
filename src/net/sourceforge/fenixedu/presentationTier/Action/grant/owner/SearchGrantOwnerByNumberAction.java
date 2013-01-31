/*
 * Created on Dec 20, 2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.grant.owner;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.grant.owner.SearchGrantOwner;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author Pica
 * @author Barbosa
 */
@Mapping(
		module = "facultyAdmOffice",
		path = "/searchGrantOwnerByNumber",
		input = "/searchGrantOwner.do?page=0&method=searchForm",
		attribute = "searchGrantOwnerByNumberForm",
		formBean = "searchGrantOwnerByNumberForm",
		scope = "request",
		parameter = "method")
@Forwards(value = {
		@Forward(
				name = "search-succesfull",
				path = "/facultyAdmOffice/grant/owner/searchGrantOwnerResultForm.jsp",
				tileProperties = @Tile(title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")),
		@Forward(name = "search-unSuccesfull", path = "/searchGrantOwner.do?page=0&method=searchForm", tileProperties = @Tile(
				title = "private.teachingstaffandresearcher.managementscholarship.scholarshipsearch")) })
public class SearchGrantOwnerByNumberAction extends FenixDispatchAction {
	public ActionForward searchGrantOwner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List infoGrantOwnerList = null;
		Integer grantOwnerNumber = null;
		// Read attributes from FormBean
		DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
		grantOwnerNumber = new Integer((String) searchGrantOwnerForm.get("grantOwnerNumber"));

		IUserView userView = UserView.getUser();
		infoGrantOwnerList = SearchGrantOwner.run(null, null, null, grantOwnerNumber, null, null);

		if (infoGrantOwnerList.isEmpty()) {
			return setError(request, mapping, "errors.grant.owner.not.found", "search-unSuccesfull", grantOwnerNumber);
		}

		request.setAttribute("infoGrantOwnerList", infoGrantOwnerList);
		return mapping.findForward("search-succesfull");
	}
}