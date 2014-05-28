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
package net.sourceforge.fenixedu.presentationTier.Action.publico.department;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.messaging.Announcement;
import net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard;
import net.sourceforge.fenixedu.domain.messaging.UnitAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.messaging.announcements.AnnouncementRSS;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "publico", path = "/department/eventsRSS", scope = "session", parameter = "method")
public class DepartmentEventsRSS extends AnnouncementRSS {

    @Override
    protected String getFeedTitle(HttpServletRequest request, AnnouncementBoard board) {
        UnitAnnouncementBoard unitBoard = (UnitAnnouncementBoard) board;
        return unitBoard == null ? "" : unitBoard.getUnit().getNameWithAcronym() + ": " + board.getName();
    }

    @Override
    protected String getDirectAnnouncementBaseUrl(HttpServletRequest request, Announcement announcement) {
        String selectedDepartment = request.getParameter("selectedDepartmentUnitID");
        return "/publico/department/events.do?method=viewEvent&selectedDepartmentUnitID=" + selectedDepartment;
    }

}
