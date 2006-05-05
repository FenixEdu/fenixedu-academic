/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.messaging.WriteThread.WriteThreadParameters;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.messaging.Forum;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
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

	updateRequestedForum(request);

	return mapping.findForward("viewForum");
    }

    
    public ActionForward prepareCreateThreadAndMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {

	this.updateRequestedForum(request);

	return mapping.findForward("createThread");
    }
    
    public ActionForward createThreadAndMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixActionException {

	try {
	    IUserView userView = this.getUserView(request);
	    Forum requestedForum = updateRequestedForum(request);

	    WriteThreadParameters parameters = new WriteThreadParameters();
	    parameters.forum = requestedForum;
	    parameters.subject = request.getParameter("subject");
	    parameters.messageText = request.getParameter("messageText");
	    parameters.creator=userView.getPerson();

	    ServiceManagerServiceFactory.executeService(userView, "WriteThread", new Object[] { parameters });
	} catch (Exception e) {
	    throw new FenixActionException(e);
	}

	return mapping.findForward("viewForuns");
    }

    private Forum updateRequestedForum(HttpServletRequest request) throws FenixActionException {
	Integer forumId = Integer.valueOf(request.getParameter("forumId"));
	try {
	    List<Forum> foruns = RootDomainObject.getInstance().getForuns();
	    Forum requestedForum = null;
	    for (Forum forum : foruns) {
		if (forum.getIdInternal().equals(forumId))
		{
		    requestedForum = forum;
		}
	    }
	    request.setAttribute("forum", requestedForum);
	    return requestedForum;
	} catch (Exception e) {
	    throw new FenixActionException(e);
	}
    }
}
