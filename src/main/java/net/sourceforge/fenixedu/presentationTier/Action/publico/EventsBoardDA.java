/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
