package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

public class ManagerAnnouncementForwardAction extends FenixAction {

	public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) {
		
		String forwardTo = "/announcements/manageUnitAnnouncementBoard.do?method=" + request.getParameter("method") + "&oid=" + request.getParameter("oid") +
		"&announcementBoardId=" + request.getParameter("announcementBoardId") + "&returnAction=" + request.getParameter("returnAction") + "&returnMethod=" +
		request.getParameter("returnMethod") + "&tabularVersion=" + request.getParameter("tabularVersion");
		
		ActionForward forward = new ActionForward(forwardTo);
		forward.setModule("/manager");
		
		return forward;
	}
}
