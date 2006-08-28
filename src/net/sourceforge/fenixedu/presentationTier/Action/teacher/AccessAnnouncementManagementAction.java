package net.sourceforge.fenixedu.presentationTier.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoSite;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author ep15
 * @author Ivo Brandão
 */
public class AccessAnnouncementManagementAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        IUserView userView = getUserView(request);

        InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);

        Object args[] = new Object[1];
        args[0] = infoSite;

        List announcements = (List) ServiceManagerServiceFactory.executeService(userView,
                "ReadAnnouncements", args);
        //remove old announcement list
        session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
        //put new announcement list
        Collections.sort(announcements);
        session.setAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST, announcements);

        session.removeAttribute("insertAnnouncementForm");

        if (!(announcements.isEmpty())) {
            return mapping.findForward("AnnouncementManagement");
        }

        session.removeAttribute(SessionConstants.INFO_SITE_ANNOUNCEMENT_LIST);
        return mapping.findForward("EditAnnouncement");
    }

}