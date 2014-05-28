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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public abstract class UnitSiteBoardsDA extends AnnouncementManagement {

    // TODO: change literal
    public static final MultiLanguageString ANNOUNCEMENTS = new MultiLanguageString().with(MultiLanguageString.pt, "Anúncios");
    public static final MultiLanguageString EVENTS = new MultiLanguageString().with(MultiLanguageString.pt, "Eventos");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        setUnitContext(request);
        setPageLanguage(request);

        return super.execute(mapping, actionForm, request, response);
    }

    protected void setUnitContext(HttpServletRequest request) {
        Unit unit = getUnit(request);
        if (unit != null) {
            request.setAttribute("unit", unit);
            request.setAttribute("site", unit.getSite());
            OldCmsSemanticURLHandler.selectSite(request, unit.getSite());
        }
    }

    private void setPageLanguage(HttpServletRequest request) {
        Boolean inEnglish;

        String inEnglishParameter = request.getParameter("inEnglish");
        if (inEnglishParameter == null) {
            inEnglish = (Boolean) request.getAttribute("inEnglish");
        } else {
            inEnglish = new Boolean(inEnglishParameter);
        }

        if (inEnglish == null) {
            inEnglish = getLocale(request).getLanguage().equals(Locale.ENGLISH.getLanguage());
        }

        request.setAttribute("inEnglish", inEnglish);
    }

    public Unit getUnit(HttpServletRequest request) {
        String parameter = request.getParameter(getContextParamName());

        if (parameter == null) {
            UnitSite site = SiteMapper.getSite(request);
            if (site != null) {
                return site.getUnit();
            }
            UnitAnnouncementBoard board = getDomainObject(request, "announcementBoardId");
            if (board != null) {
                return board.getUnit();
            }
            Announcement announcement = getDomainObject(request, "announcementId");
            if (announcement != null) {
                return ((UnitAnnouncementBoard) announcement.getAnnouncementBoard()).getUnit();
            }
        }

        try {
            return (Unit) FenixFramework.getDomainObject(parameter);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        addExtraParameter(request, builder, getContextParamName());

        return builder.toString();
    }

    private void addExtraParameter(HttpServletRequest request, StringBuilder builder, String name) {
        String parameter = request.getParameter(name);
        if (parameter != null) {
            if (builder.length() != 0) {
                builder.append("&amp;");
            }

            builder.append(name + "=" + parameter);
        }
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        Unit unit = getUnit(request);
        List<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();

        if (unit == null) {
            return boards;
        }

        User userView = getUserView(request);
        for (AnnouncementBoard board : unit.getBoards()) {
            if (board.getReaders() == null) {
                boards.add(board);
            }

            if (board.getReaders().isMember(userView)) {
                boards.add(board);
            }
        }

        return boards;
    }

    @Override
    protected AnnouncementBoard getRequestedAnnouncementBoard(HttpServletRequest request) {
        Unit unit = getUnit(request);

        if (unit == null) {
            return null;
        } else {
            MultiLanguageString name = getBoardName(request);
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getReaders() == null && board.getName().equalInAnyLanguage(name)) {
                    return board;
                }
            }

            return null;
        }
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        AnnouncementBoard board = getRequestedAnnouncementBoard(request);

        if (board != null) {
            return super.viewAnnouncements(mapping, form, request, response);
        } else {
            return mapping.findForward("listAnnouncements");
        }
    }

    public ActionForward viewEvent(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return viewAnnouncement(mapping, form, request, response);
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        String path = getActionPath(mapping, request);

        request.setAttribute("announcementActionVariable", path);

        if (isShowingAnnouncements(request)) {
            request.setAttribute("showingAnnouncements", true);
        }

        if (isShowingEvents(request)) {
            request.setAttribute("showingEvents", true);
        }

        return path;
    }

    protected boolean isShowingAnnouncements(HttpServletRequest request) {
        return getBoardName(request).equalInAnyLanguage(ANNOUNCEMENTS);
    }

    protected boolean isShowingEvents(HttpServletRequest request) {
        return getBoardName(request).equalInAnyLanguage(EVENTS);
    }

    public String getContextParamName() {
        return "unitID";
    }

    protected abstract MultiLanguageString getBoardName(HttpServletRequest request);

    protected String getActionPath(ActionMapping mapping, HttpServletRequest request) {
        return mapping.getPath() + ".do";
    }

}
