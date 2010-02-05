package net.sourceforge.fenixedu.presentationTier.Action.commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public abstract class AbstractManageThesisDA extends FenixDispatchAction {

    public ActionForward viewOperationsThesis(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	request.setAttribute("thesis", getThesis(request));
	return mapping.findForward("viewOperationsThesis");
    }

    protected Integer getId(String id) {
	if (id == null || id.equals("")) {
	    return null;
	}

	try {
	    return new Integer(id);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    protected Thesis getThesis(HttpServletRequest request) {
	Integer id = getId(request.getParameter("thesisID"));
	if (id == null) {
	    return null;
	} else {
	    return RootDomainObject.getInstance().readThesisByOID(id);
	}
    }
}