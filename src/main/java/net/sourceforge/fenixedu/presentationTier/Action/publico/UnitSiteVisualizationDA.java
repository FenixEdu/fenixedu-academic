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

import java.net.MalformedURLException;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.Site.SiteMapper;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.manager.SiteVisualizationDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.RequestUtils;
import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

import com.google.common.base.Strings;

@Mapping(module = "publico", path = "/units/viewSite")
@Forwards({ @Forward(path = "basicUnit-site-front-page-banner-intro", name = "frontPage-BANNER_INTRO"),
        @Forward(path = "basicUnit-site-front-page-intro-banner", name = "frontPage-INTRO_BANNER"),
        @Forward(path = "basicUnit-site-front-page-intro-float", name = "frontPage-BANNER_INTRO_COLLAPSED"),
        @Forward(path = "basicUnit-section", name = "site-section"),
        @Forward(path = "basicUnit-section-deny", name = "site-section-deny"),
        @Forward(path = "basicUnit-section-adviseLogin", name = "site-section-adviseLogin"),
        @Forward(path = "basicUnit-item", name = "site-item"), @Forward(path = "basicUnit-item-deny", name = "site-item-deny"),
        @Forward(path = "basicUnit-item-adviseLogin", name = "site-item-adviseLogin"),
        @Forward(path = "basicUnit-organization", name = "unit-organization"),
        @Forward(path = "/units/announcements.do", name = "announcementsAction"),
        @Forward(path = "/units/events.do", name = "eventsAction"),
        @Forward(path = "/units/announcementsRSS.do", name = "announcementsRSSAction"),
        @Forward(path = "/units/eventsRSS.do", name = "eventsRSSAction") })
public class UnitSiteVisualizationDA extends SiteVisualizationDA {

    public static final int ANNOUNCEMENTS_NUMBER = 3;

    public static final MultiLanguageString ANNOUNCEMENTS_NAME = new MultiLanguageString().with(MultiLanguageString.pt,
            "Anúncios");

    public static final MultiLanguageString EVENTS_NAME = new MultiLanguageString().with(MultiLanguageString.pt, "Eventos");

    @Override
    protected ActionForward getSiteDefaultView(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return presentation(mapping, form, request, response);
    }

    @Override
    protected String getDirectLinkContext(HttpServletRequest request) {
        Unit unit = getUnit(request);
        Site site = unit.getSite();
        try {
            return site == null ? null : RequestUtils.absoluteURL(request, site.getReversePath()).toString();
        } catch (MalformedURLException e) {
            return null;
        }
    }

    protected String getContextParamName(HttpServletRequest request) {
        return "unitID";
    }

    protected Object getContextParamValue(HttpServletRequest request) {
        return getUnit(request).getExternalId();
    }

    protected String getMappingPath(ActionMapping mapping, String name) {
        ActionForward forward = mapping.findForward(name);

        return forward == null ? null : forward.getPath();
    }

    public ActionForward presentation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Unit unit = getUnit(request);
        UnitSite site = unit.getSite();

        AnnouncementBoard announcementsBoard = null;
        AnnouncementBoard eventsBoard = null;

        for (AnnouncementBoard unitBoard : unit.getBoards()) {
            if (unitBoard.isPublicToRead() && unitBoard.getName().equalInAnyLanguage(ANNOUNCEMENTS_NAME)) {
                announcementsBoard = unitBoard;
            }

            if (unitBoard.isPublicToRead() && unitBoard.getName().equalInAnyLanguage(EVENTS_NAME)) {
                eventsBoard = unitBoard;
            }
        }

        if (announcementsBoard != null) {
            List<Announcement> announcements = announcementsBoard.getActiveAnnouncements();
            announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER));
            request.setAttribute("announcements", announcements);
        }

        if (eventsBoard != null) {
            request.setAttribute("announcementBoard", eventsBoard);

            YearMonthDay currentDay = new YearMonthDay();
            List<Announcement> currentDayAnnouncements = eventsBoard.getActiveAnnouncementsFor(currentDay);
            List<Announcement> futureAnnouncements = eventsBoard.getActiveAnnouncementsAfter(currentDay);

            Collections.sort(futureAnnouncements, Announcement.SUBJECT_BEGIN_DATE);
            request.setAttribute("today-events", currentDayAnnouncements);
            request.setAttribute("future-events", futureAnnouncements);

            int eventCount = currentDayAnnouncements.size() + futureAnnouncements.size();
            if (eventCount < ANNOUNCEMENTS_NUMBER) {
                List<Announcement> announcements = eventsBoard.getActiveAnnouncementsBefore(currentDay);
                announcements = announcements.subList(0, Math.min(announcements.size(), ANNOUNCEMENTS_NUMBER - eventCount));
                request.setAttribute("eventAnnouncements", announcements);
            }
        }

        return mapping.findForward("frontPage-" + site.getLayout());
    }

    protected UnitSite getUnitSite(HttpServletRequest request) {
        return SiteMapper.getSite(request);
    }

    protected Unit getUnit(HttpServletRequest request) {
        Unit unit = (Unit) request.getAttribute("unit");

        if (unit == null) {
            UnitSite site = SiteMapper.getSite(request);
            if (site == null) {
                String siteId = FenixContextDispatchAction.getFromRequest("siteID", request);
                if (!Strings.isNullOrEmpty(siteId)) {
                    site = FenixFramework.getDomainObject(siteId);
                    OldCmsSemanticURLHandler.selectSite(request, site);
                }
            }
            unit = site.getUnit();
            request.setAttribute("unit", unit);
        }

        return unit;
    }

    public ActionForward organization(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("unit-organization");
    }

    public ActionForward subunits(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        Unit unit = getUnit(request);

        SortedSet<Unit> subunits = new TreeSet<Unit>(Unit.COMPARATOR_BY_NAME_AND_ID);
        for (Unit sub : unit.getSubUnits()) {
            if (sub.hasSite()) {
                subunits.add(sub);

                String siteUrl = getSiteUrl(mapping, sub);
                request.setAttribute("viewSite" + sub.getExternalId(), siteUrl);
            }
        }

        request.setAttribute("subunits", subunits);
        return mapping.findForward("unit-subunits");
    }

    protected String getSiteUrl(ActionMapping mapping, Unit sub) {
        ActionForward forward = mapping.findForward("view" + sub.getClass().getSimpleName() + "Site");

        if (forward == null) {
            forward = mapping.findForward("viewUnitSite");
        }

        return String.format(forward.getPath(), sub.getExternalId(), sub.getSite().getExternalId());
    }
}
