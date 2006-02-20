package net.sourceforge.fenixedu.presentationTier.Action.research;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageResearchDA extends FenixDispatchAction {

    public ActionForward viewResearch(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        //final Object[] args = { Space.class };
        //final Collection<Space> spaces = (Collection<Space>) ServiceUtils.executeService(userView, "ReadAllDomainObjects", args);
        //request.setAttribute("spaces", spaces);
        return mapping.findForward("Success");
    }

}