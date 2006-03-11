package net.sourceforge.fenixedu.presentationTier.Action.publico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Homepage;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ViewHomepageDA extends FenixContextDispatchAction {

    public ActionForward show(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final String homepageIDString = request.getParameter("homepageID");
    	final Integer homepageID = Integer.valueOf(homepageIDString);
    	final Homepage homepage = (Homepage) readDomainObject(request, Homepage.class, homepageID);
    	request.setAttribute("homepage", homepage);
        return mapping.findForward("view-homepage");
    }

    public ActionForward notFound(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return mapping.findForward("not-found-homepage");
    }

}