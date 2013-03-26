package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.dto.AnnouncementArchive;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class EventsBoardDA extends UnitSiteBoardsDA {

    @Override
    protected MultiLanguageString getBoardName(HttpServletRequest request) {
        return UnitSiteBoardsDA.EVENTS;
    }

    public ActionForward viewEvents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return viewAnnouncements(mapping, form, request, response);
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        String path = getActionPath(mapping, request);

        request.setAttribute("eventActionVariable", path);

        if (isShowingAnnouncements(request)) {
            request.setAttribute("showingAnnouncements", true);
        }

        if (isShowingEvents(request)) {
            request.setAttribute("showingEvents", true);
        }

        return path;
    }

    @Override
    public ActionForward viewArchive(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Integer selectedArchiveYear = this.getSelectedArchiveYear(request);
        Integer selectedArchiveMonth = this.getSelectedArchiveMonth(request);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, selectedArchiveMonth - 1);
        calendar.set(Calendar.YEAR, selectedArchiveYear);
        request.setAttribute("archiveDate", calendar.getTime());

        AnnouncementArchive archive = this.buildArchive(this.getRequestedAnnouncementBoard(request), request);
        this.viewAnnouncements(mapping, form, request, response);
        request.setAttribute("announcements",
                archive.getEntries().get(selectedArchiveYear).getEntries().get((selectedArchiveMonth)).getAnnouncements());

        return mapping.findForward("listAnnouncements");
    }
}
