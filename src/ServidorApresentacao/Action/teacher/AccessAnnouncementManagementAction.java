package ServidorApresentacao.Action.teacher;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoSite;
import ServidorAplicacao.Servico.UserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author ep15
 * @author Ivo Brandão
 */
public class AccessAnnouncementManagementAction extends FenixAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);
        session.removeAttribute(SessionConstants.INFO_SECTION);
        UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

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