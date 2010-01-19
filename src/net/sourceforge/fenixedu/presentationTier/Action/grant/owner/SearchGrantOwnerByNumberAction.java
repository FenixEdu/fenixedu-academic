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

/**
 * @author Pica
 * @author Barbosa
 */
public class SearchGrantOwnerByNumberAction extends FenixDispatchAction {
    public ActionForward searchGrantOwner(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	List infoGrantOwnerList = null;
	Integer grantOwnerNumber = null;
	// Read attributes from FormBean
	DynaValidatorForm searchGrantOwnerForm = (DynaValidatorForm) form;
	grantOwnerNumber = new Integer((String) searchGrantOwnerForm.get("grantOwnerNumber"));

	IUserView userView = UserView.getUser();
	infoGrantOwnerList = (List) SearchGrantOwner.run(null, null, null, grantOwnerNumber, null, null);

	if (infoGrantOwnerList.isEmpty()) {
	    return setError(request, mapping, "errors.grant.owner.not.found", "search-unSuccesfull", grantOwnerNumber);
	}

	request.setAttribute("infoGrantOwnerList", infoGrantOwnerList);
	return mapping.findForward("search-succesfull");
    }
}