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
package net.sourceforge.fenixedu.presentationTier.Action.publico.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseAnnouncementBoard;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.DomainObject;
import pt.ist.fenixframework.FenixFramework;

@Mapping(module = "publico", path = "/announcementsRSS")
public class GenerateAnnoucementsRSS extends FenixDispatchAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final String executionCourseIdString = request.getParameter("id");
        final DomainObject obj = FenixFramework.getDomainObject(executionCourseIdString);
        if (!(obj instanceof ExecutionCourse)) {
            return forward("/notFound.jsp");
        }
        ExecutionCourse executionCourse = (ExecutionCourse) obj;
        final ExecutionCourseAnnouncementBoard announcementBoard = executionCourse.getBoard();
        if (announcementBoard == null) {
            return forward("/publico/executionCourse.do?method=notFound&executionCourseID=" + executionCourse.getExternalId());
        }
        return forward("/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getExternalId());
    }

    private ActionForward forward(String purl) {
        return new ActionForward(purl);
    }

}