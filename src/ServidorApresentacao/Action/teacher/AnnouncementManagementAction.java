package ServidorApresentacao.Action.teacher;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorApresentacao.Action.FenixAction;
/**
 * @author ep15
 * @author Ivo Brandão
 */
public class AnnouncementManagementAction extends FenixAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form,	
    	HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = getSession(request);
		
		System.out.println("AnnouncementManagementAction" + form);
		DynaActionForm announcementManagementForm = (DynaActionForm) form;
		
        String option = (String) announcementManagementForm.get("option");
        int index = ((Integer) announcementManagementForm.get("index")).intValue();
        List announcements = (List) session.getAttribute("Announcements");
        if (option.equals("Edit")) {
            session.setAttribute("Announcement", announcements.get(index));
            return mapping.findForward("EditAnnouncement");
        }
        else if (option.equals("Delete")) {
            session.setAttribute("Announcement", announcements.get(index));
            return mapping.findForward("DeleteAnnouncement");
        }
        else
            return mapping.findForward("InsertAnnouncement");
    }
}