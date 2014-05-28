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
package net.sourceforge.fenixedu.presentationTier.Action.webSiteManager;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.AnnouncementManagement;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;

@Forwards({ @Forward(name = "viewAnnouncement", path = "/messaging/announcements/viewAnnouncement.jsp"),
        @Forward(name = "uploadFile", path = "/messaging/announcements/uploadFileToBoard.jsp"),
        @Forward(name = "edit", path = "/messaging/announcements/editAnnouncement.jsp"),
        @Forward(name = "listAnnouncements", path = "/messaging/announcements/listBoardAnnouncements.jsp"),
        @Forward(name = "add", path = "/messaging/announcements/addAnnouncement.jsp"),
        @Forward(name = "noBoards", path = "/webSiteManager/commons/noBoards.jsp"),
        @Forward(name = "editFile", path = "/messaging/announcements/editFileInBoard.jsp"),
        @Forward(name = "listAnnouncementBoards", path = "/webSiteManager/commons/listAnnouncementBoards.jsp") })
public abstract class UnitSiteAnnouncementManagement extends AnnouncementManagement {

    private static final Logger logger = LoggerFactory.getLogger(UnitSiteAnnouncementManagement.class);

    private static final ActionForward FORWARD = new ActionForward("/websiteFrame.jsp");

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("site", getSite(request));
        ActionForward forward = super.execute(mapping, actionForm, request, response);
        if (forward.getPath().endsWith(".jsp")) {
            request.setAttribute("actual$page", forward.getPath());
            return FORWARD;
        } else {
            return forward;
        }
    }

    private Integer getId(String id) {
        if (id == null || id.equals("")) {
            return null;
        }

        try {
            return new Integer(id);
        } catch (NumberFormatException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    protected UnitSite getSite(HttpServletRequest request) {
        return getDomainObject(request, "oid");
    }

    protected Unit getUnit(HttpServletRequest request) {
        UnitSite site = getSite(request);
        if (site == null) {
            return null;
        } else {
            return site.getUnit();
        }
    }

    public ActionForward viewBoards(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        UnitSite site = getSite(request);
        Unit unit = site.getUnit();

        if (unit == null || unit.getBoards().isEmpty()) {
            return mapping.findForward("noBoards");
        } else {
            Collection<UnitAnnouncementBoard> boards = unit.getBoards();
            if (boards.size() > 1) {
                return start(mapping, actionForm, request, response);
            } else {
                AnnouncementBoard board = boards.iterator().next();

                ActionForward forward = new ActionForward(mapping.findForward("viewAnnouncementsRedirect"));
                forward.setPath(forward.getPath()
                        + String.format("&announcementBoardId=%s&oid=%s", board.getExternalId(), site.getExternalId()));

                return forward;
            }
        }
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder builder = new StringBuilder();

        addExtraParameter(request, builder, "tabularVersion");
        addExtraParameter(request, builder, "oid");

        return builder.toString();
    }

    protected void addExtraParameter(HttpServletRequest request, StringBuilder builder, String name) {
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

        Collection<AnnouncementBoard> boards = new ArrayList<AnnouncementBoard>();
        if (unit != null) {
            for (AnnouncementBoard board : unit.getBoards()) {
                if (board.getWriters() == null || board.getReaders() == null || board.getManagers() == null
                        || board.getWriters().isMember(getUserView(request)) || board.getReaders().isMember(getUserView(request))
                        || board.getManagers().isMember(getUserView(request))) {
                    boards.add(board);
                }
            }

        }

        return boards;
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.addAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward editAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.editAnnouncement(mapping, form, request, response);
    }

    @Override
    public ActionForward viewAnnouncements(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");

        return super.viewAnnouncements(mapping, form, request, response);
    }

}
