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
/*
 * Author : Goncalo Luiz
 * Creation Date: Jul 4, 2006,3:26:38 PM
 */
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard.AnnouncementPresentationBean;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessLevel;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoardAccessType;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.MessagingApplication.MessagingAnnouncementsApp;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.security.Authenticate;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;
import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.utl.ist.fenix.tools.util.CollectionPager;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a><br>
 * <br>
 *         Created on Jul 4, 2006,3:26:38 PM
 * 
 */
@StrutsFunctionality(app = MessagingAnnouncementsApp.class, path = "news", titleKey = "messaging.menu.news.link")
@Mapping(module = "messaging", path = "/announcements/announcementsStartPageHandler",
        formBeanClass = AnnouncementsStartPageForm.class)
@Forwards({ @Forward(name = "viewAnnouncement", path = "/messaging/announcements/viewAnnouncement.jsp"),
        @Forward(name = "uploadFile", path = "/messaging/announcements/uploadFileToBoard.jsp"),
        @Forward(name = "boardListingWithCriteria", path = "/messaging/announcements/boardListingWithCriteria.jsp"),
        @Forward(name = "news", path = "/messaging/announcements/news.jsp"),
        @Forward(name = "edit", path = "/messaging/announcements/editAnnouncement.jsp"),
        @Forward(name = "listAnnouncements", path = "/messaging/announcements/listBoardAnnouncements.jsp"),
        @Forward(name = "add", path = "/messaging/announcements/addAnnouncement.jsp"),
        @Forward(name = "startPage", path = "/messaging/announcements/startPage.jsp"),
        @Forward(name = "editFile", path = "/messaging/announcements/editFileInBoard.jsp"),
        @Forward(name = "listAnnouncementBoards", path = "/messaging/announcements/listAnnouncementBoards.jsp") })
public class AnnouncementsStartPageHandler extends AnnouncementManagement {

    private static final int RECENT_BOARDS_TO_SHOW = 40;
    private static final int PAGE_SIZE = 20;

    @EntryPoint
    public ActionForward news(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("returnMethod", "news");
        request.setAttribute("announcements", getAnnouncementsToShow(request, actionForm));
        // request.setAttribute("announcementBoards", boardsToView(request));

        return mapping.findForward("news");
    }

    @Override
    public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        request.setAttribute("returnMethod", "start");
        request.setAttribute("bookmarkedAnnouncementBoards", getSortedBookmarkedBoards(request));
        request.setAttribute("currentExecutionCoursesAnnouncementBoards", getCurrentExecutionCoursesAnnouncementBoards(request));

        return mapping.findForward("startPage");
    }

    @StrutsFunctionality(app = MessagingAnnouncementsApp.class, path = "favourites", titleKey = "messaging.menu.favourites.link")
    @Mapping(path = "/manageFavoriteBoards", module = "messaging")
    public static class ManageFavoriteBoardsDA extends AnnouncementsStartPageHandler {
        @Override
        @EntryPoint
        public ActionForward start(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
                HttpServletResponse response) throws Exception {
            return super.start(mapping, actionForm, request, response);
        }
    }

    private List<AnnouncementBoard> getSortedBookmarkedBoards(final HttpServletRequest request) {
        final List<AnnouncementBoard> result =
                new ArrayList<AnnouncementBoard>(getLoggedPerson(request).getBookmarkedBoardsSet());
        Collections.sort(result, AnnouncementBoard.BY_NAME);
        return result;
    }

    private Collection<AnnouncementBoard> getCurrentExecutionCoursesAnnouncementBoards(final HttpServletRequest request) {
        final List<AnnouncementBoard> result =
                new ArrayList<AnnouncementBoard>(getLoggedPerson(request).getCurrentExecutionCoursesAnnouncementBoards());
        Collections.sort(result, AnnouncementBoard.BY_NAME);
        return result;
    }

    private List<AnnouncementBoard> getRecentBoards(HttpServletRequest request) {

        final List<AnnouncementBoard> result = new ArrayList<AnnouncementBoard>();

        final DateTime startDate = getStartDate(request);
        int toShowCount = AnnouncementsStartPageHandler.RECENT_BOARDS_TO_SHOW;

        for (final AnnouncementBoard board : Bennu.getInstance().getAnnouncementBoardSet()) {
            if (board.hasReader(getLoggedPerson(request)) || board.hasWriter(getLoggedPerson(request))) {
                if (toShowCount == 0 || (startDate != null && board.getCreationDate().isBefore(startDate))) {
                    break;
                }
                result.add(board);
                toShowCount--;
            }
        }

        Collections.sort(result, AnnouncementBoard.NEWEST_FIRST);

        return result;
    }

    private DateTime getStartDate(HttpServletRequest request) {

        final String selectedTimeSpanString = request.getParameter("recentBoardsTimeSpanSelection");
        final RecentBoardsTimeSpanSelection selectedTimeSpan =
                (selectedTimeSpanString != null) ? RecentBoardsTimeSpanSelection.valueOf(selectedTimeSpanString) : RecentBoardsTimeSpanSelection.TS_LAST_WEEK;

        final DateTime now = new DateTime();
        DateTime startDate = null;

        switch (selectedTimeSpan) {
        case TS_ALL_ACTIVE:
            break;
        case TS_LAST_WEEK:
            startDate = now.minusDays(7);
            break;
        case TS_ONE_MONTH:
            startDate = now.minusDays(30);
            break;
        case TS_TWO_MONTHS:
            startDate = now.minusDays(60);
            break;
        case TS_TODAY:
            startDate = now.minusHours(now.hourOfDay().get());
            startDate = startDate.minusMinutes(now.minuteOfHour().get());
            startDate = startDate.minusSeconds(now.secondOfMinute().get());
            break;
        case TS_TWO_WEEKS:
            startDate = now.minusDays(15);
            break;
        case TS_YESTERDAY:
            startDate = now.minusDays(1);
            break;
        }
        return startDate;
    }

    private Collection<Announcement> getAnnouncementsToShow(HttpServletRequest request, ActionForm actionForm) {

        final AnnouncementsStartPageForm form = (AnnouncementsStartPageForm) actionForm;

        final AnnouncementPresentationBean announcementPresentationBean =
                new AnnouncementPresentationBean(form.getHowManyAnnouncementsToShow(), Announcement.NEWEST_FIRST);

        if (form.getBoardType().equals("BOOKMARKED")) {
            getBookmarkedAnnouncements(request, announcementPresentationBean);
        }

        if (form.getBoardType().equals("INSTITUTIONAL") || announcementPresentationBean.isEmpty()) {
            getInstitutionalAnnouncements(request, announcementPresentationBean);
            form.setBoardType("INSTITUTIONAL"); // because bean could be empty,
            // must set this value
        }

        return announcementPresentationBean;
    }

    private void getInstitutionalAnnouncements(HttpServletRequest request,
            final AnnouncementPresentationBean announcementPresentationBean) {
        if (rootDomainObject.getInstitutionUnit() != null) {
            for (final AnnouncementBoard board : rootDomainObject.getInstitutionUnit().getBoardsSet()) {
                addBoardAnnouncements(request, board, announcementPresentationBean);
            }
        }
    }

    private void getBookmarkedAnnouncements(HttpServletRequest request,
            final AnnouncementPresentationBean announcementPresentationBean) {
        for (final AnnouncementBoard board : getLoggedPerson(request).getBookmarkedBoardsSet()) {
            addBoardAnnouncements(request, board, announcementPresentationBean);
        }
    }

    private void addBoardAnnouncements(HttpServletRequest request, final AnnouncementBoard board,
            final AnnouncementPresentationBean announcementPresentationBean) {
        final Person person = getLoggedPerson(request);
        if (board.hasReader(person) || board.hasWriter(person)) {
            board.addVisibleAnnouncements(announcementPresentationBean);
        }
    }

    @Override
    protected String getExtraRequestParameters(HttpServletRequest request) {
        StringBuilder buffer = new StringBuilder();
        String announcementBoardAccessType = request.getParameter("announcementBoardAccessType");
        String announcementBoardAccessLevel = request.getParameter("announcementBoardAccessLevel");

        if (announcementBoardAccessType != null) {
            buffer.append("&amp;announcementBoardAccessType=").append(announcementBoardAccessType);
        }
        if (announcementBoardAccessLevel != null) {
            buffer.append("&amp;announcementBoardAccessLevel=").append(announcementBoardAccessLevel);
        }
        if (request.getParameter("howManyAnnouncementsToShow") != null) {
            buffer.append("&amp;howManyAnnouncementsToShow=").append(request.getParameter("howManyAnnouncementsToShow"));
        }
        if (request.getParameter("recentBoardsTimeSpanSelection") != null) {
            buffer.append("&amp;recentBoardsTimeSpanSelection=").append(request.getParameter("recentBoardsTimeSpanSelection"));
        }
        if (request.getParameter("boardType") != null) {
            buffer.append("&amp;boardType=").append(request.getParameter("boardType"));
        }

        return buffer.toString();
    }

    @Override
    protected String getContextInformation(ActionMapping mapping, HttpServletRequest request) {
        return "/announcements/announcementsStartPageHandler.do";
    }

    @Override
    protected Collection<AnnouncementBoard> boardsToView(HttpServletRequest request) throws Exception {
        return getRecentBoards(request);
    }

    public ActionForward handleBoardListing(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        final AnnouncementsStartPageForm form = (AnnouncementsStartPageForm) actionForm;

        Collection<UnitAnnouncementBoard> unitAnnouncementBoards =
                new TreeSet<UnitAnnouncementBoard>(UnitAnnouncementBoard.BY_UNIT_DEPTH_AND_NAME);
        Collection<ExecutionCourseAnnouncementBoard> executionCourseAnnouncementBoards =
                new TreeSet<ExecutionCourseAnnouncementBoard>(
                        ExecutionCourseAnnouncementBoard.COMPARE_BY_EXECUTION_PERIOD_AND_NAME);

        for (final AnnouncementBoard board : Bennu.getInstance().getAnnouncementBoardSet()) {
            if (board.hasReaderOrWriter(getLoggedPerson(request))) {
                if (board instanceof UnitAnnouncementBoard) {
                    unitAnnouncementBoards.add((UnitAnnouncementBoard) board);

                } else if (board instanceof ExecutionCourseAnnouncementBoard) {
                    ExecutionCourseAnnouncementBoard executionCourseBoard = (ExecutionCourseAnnouncementBoard) board;
                    if (executionCourseBoard.hasExecutionCourse()
                            && executionCourseBoard.getExecutionCourse().getExecutionPeriod().getState()
                                    .equals(PeriodState.CURRENT)) {
                        executionCourseAnnouncementBoards.add(executionCourseBoard);
                    }
                }
            }
        }

        final AnnouncementBoardAccessLevel level = AnnouncementBoardAccessLevel.valueOf(form.getAnnouncementBoardAccessLevel());
        final AnnouncementBoardAccessType type = AnnouncementBoardAccessType.valueOf(form.getAnnouncementBoardAccessType());

        unitAnnouncementBoards =
                (Collection<UnitAnnouncementBoard>) filterAnnouncementBoardsByLevelAndType(request, unitAnnouncementBoards,
                        level, type);
        executionCourseAnnouncementBoards =
                (Collection<ExecutionCourseAnnouncementBoard>) filterAnnouncementBoardsByLevelAndType(request,
                        executionCourseAnnouncementBoards, level, type);

        request.setAttribute("unitAnnouncementBoards", getPagedUnitAnnouncementBoard(unitAnnouncementBoards, request));
        request.setAttribute("executionCourseAnnouncementBoards",
                getPagedExecutionCourseAnnouncementBoard(executionCourseAnnouncementBoards, request));
        request.setAttribute("returnMethod", "handleBoardListing");

        return mapping.findForward("boardListingWithCriteria");
    }

    private Collection<? extends AnnouncementBoard> filterAnnouncementBoardsByLevelAndType(HttpServletRequest request,
            Collection<? extends AnnouncementBoard> announcementBoards, final AnnouncementBoardAccessLevel level,
            final AnnouncementBoardAccessType type) {
        announcementBoards =
                CollectionUtils.select(announcementBoards,
                        new AnnouncementBoard.AcessLevelPredicate(level, Authenticate.getUser()));
        announcementBoards =
                CollectionUtils.select(announcementBoards, new AnnouncementBoard.AcessTypePredicate(type,
                        getLoggedPerson(request)));
        return announcementBoards;
    }

    private Collection<UnitAnnouncementBoard> getPagedUnitAnnouncementBoard(
            Collection<UnitAnnouncementBoard> unitAnnouncementBoards, HttpServletRequest request) {
        final String pageNumberString = request.getParameter("pageNumberUnitBoards");
        final Integer pageNumber =
                pageNumberString != null && pageNumberString.length() > 0 ? Integer.valueOf(pageNumberString) : Integer
                        .valueOf(1);
        request.setAttribute("pageNumberUnitBoards", pageNumber);
        final CollectionPager<UnitAnnouncementBoard> unitBoardsPager =
                new CollectionPager<UnitAnnouncementBoard>(unitAnnouncementBoards, PAGE_SIZE);
        request.setAttribute("numberOfPagesUnitBoards", Integer.valueOf(unitBoardsPager.getNumberOfPages()));
        return unitBoardsPager.getPage(pageNumber);
    }

    private Collection<ExecutionCourseAnnouncementBoard> getPagedExecutionCourseAnnouncementBoard(
            Collection<ExecutionCourseAnnouncementBoard> executionCourseAnnouncementBoards, HttpServletRequest request) {
        final String pageNumberString = request.getParameter("pageNumberExecutionCourseBoards");
        final Integer pageNumber =
                pageNumberString != null && pageNumberString.length() > 0 ? Integer.valueOf(pageNumberString) : Integer
                        .valueOf(1);
        request.setAttribute("pageNumberExecutionCourseBoards", pageNumber);

        final CollectionPager<ExecutionCourseAnnouncementBoard> executionCourseBoardsPager =
                new CollectionPager<ExecutionCourseAnnouncementBoard>(executionCourseAnnouncementBoards, PAGE_SIZE);
        request.setAttribute("numberOfPagesExecutionCourseBoards", Integer.valueOf(executionCourseBoardsPager.getNumberOfPages()));
        return executionCourseBoardsPager.getPage(pageNumber);
    }

    @Override
    public ActionForward removeBookmark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        super.removeBookmark(request);

        final String returnMethod = getReturnMethod(request);
        if (returnMethod != null) {
            if (returnMethod.equals("news")) {
                return this.news(mapping, form, request, response);
            } else if (returnMethod.equals("start")) {
                return this.start(mapping, form, request, response);
            } else if (returnMethod.equals("handleBoardListing")) {
                return this.handleBoardListing(mapping, form, request, response);
            }
        }
        return this.start(mapping, form, request, response);
    }

    @Override
    public ActionForward addBookmark(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        super.createBookmark(request);
        final String returnMethod = getReturnMethod(request);
        if (returnMethod != null) {
            if (returnMethod.equals("news")) {
                return this.news(mapping, actionForm, request, response);
            } else if (returnMethod.equals("handleBoardListing")) {
                return this.handleBoardListing(mapping, actionForm, request, response);
            }
        }
        return this.start(mapping, actionForm, request, response);
    }

    private String getReturnMethod(HttpServletRequest request) {
        return (request.getAttribute("returnMethod") != null) ? request.getAttribute("returnMethod").toString() : request
                .getParameter("returnMethod");
    }

    @Override
    public ActionForward addAnnouncement(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        request.setAttribute("returnMethod", "viewAnnouncements");
        return super.addAnnouncement(mapping, form, request, response);
    }

}