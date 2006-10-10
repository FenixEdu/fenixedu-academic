/**
 * Author : Goncalo Luiz
 * Creation Date: Jun 26, 2006,3:07:21 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 *         <br>
 *         Created on Jun 26, 2006,3:07:21 PM
 * 
 */
public class UnitAnnouncementBoardsManagement
	extends
	net.sourceforge.fenixedu.presentationTier.Action.manager.messaging.announcements.UnitAnnouncementBoardsManagement {

    @Override
    public ActionForward deleteAnnouncementBoard(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws Exception {
	
	if (!getLoggedPerson(request).hasRole(RoleType.MANAGER)) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
                    "error.not.allowed.to.delete.board"));
            saveErrors(request, actionMessages);
            
            return super.editAnnouncementBoard(mapping, actionForm, request, response);
        }
	
	ServiceUtils.executeService(getUserView(request), "DeleteAnnouncementBoard",
		new Object[] { this.getRequestedAnnouncementBoard(request) });
	
	return this.start(mapping, actionForm, request, response);
    }
}
