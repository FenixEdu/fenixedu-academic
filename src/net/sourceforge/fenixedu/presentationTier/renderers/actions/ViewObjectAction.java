package net.sourceforge.fenixedu.presentationTier.renderers.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.DomainObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewObjectAction extends NavigationAction {

    public ViewObjectAction() {
	super();
    }

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {
	DomainObject domainObject = getTargetObject(request);

	request.setAttribute("object", domainObject);

	request.setAttribute("schema", getGivenSchema(request));
	request.setAttribute("layout", getGivenLayout(request));

	return mapping.findForward(NAVIGATION_SHOW);
    }
}
