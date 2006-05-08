/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/> Created
 *         on May 5, 2006, 10:42:00 AM
 * 
 */
public class ForunsManagement extends FenixDispatchAction {

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        try {
            List<Forum> foruns = RootDomainObject.getInstance().getForuns();
            request.setAttribute("foruns", foruns);
        } catch (Exception e) {
            throw new FenixActionException(e);
        }

        return mapping.findForward("viewForuns");
    }

    public ActionForward viewForum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        Integer forumId = getForumId(request);
        Forum forum = rootDomainObject.readForumByOID(forumId);

        request.setAttribute("forum", forum);

        return mapping.findForward("viewForum");
    }

    public Integer getForumId(HttpServletRequest request) {
        return Integer.valueOf(request.getParameter("forumId"));
    }

}
