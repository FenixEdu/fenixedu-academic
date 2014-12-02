/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.faces.bean.publico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.space.WrittenEvaluationSpaceOccupation;
import org.fenixedu.academic.dto.InfoRoom;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.ui.faces.components.util.CalendarLink;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.Space;
import org.fenixedu.spaces.domain.occupation.Occupation;

public class WrittenEvaluationsByRoomBackingBean extends
        org.fenixedu.academic.ui.faces.bean.sop.evaluation.WrittenEvaluationsByRoomBackingBean {

    @Override
    public Map<InfoRoom, List<CalendarLink>> getWrittenEvaluationCalendarLinks() throws FenixServiceException {
        final Collection<InfoRoom> rooms = getRoomsToDisplayMap();
        if (rooms != null) {
            final Map<InfoRoom, List<CalendarLink>> calendarLinksMap = new HashMap<InfoRoom, List<CalendarLink>>();
            for (final InfoRoom infoRoom : rooms) {
                Space room = infoRoom.getRoom();
                final List<CalendarLink> calendarLinks = new ArrayList<CalendarLink>();
                for (final Occupation roomOccupation : room.getOccupationSet()) {
                    if (roomOccupation instanceof WrittenEvaluationSpaceOccupation) {
                        Collection<WrittenEvaluation> writtenEvaluations =
                                ((WrittenEvaluationSpaceOccupation) roomOccupation).getWrittenEvaluationsSet();
                        for (WrittenEvaluation writtenEvaluation : writtenEvaluations) {
                            if (verifyWrittenEvaluationExecutionPeriod(writtenEvaluation, getAcademicIntervalObject(), null)) {
                                final ExecutionCourse executionCourse =
                                        writtenEvaluation.getAssociatedExecutionCoursesSet().iterator().next();
                                final CalendarLink calendarLink =
                                        new CalendarLink(executionCourse, writtenEvaluation, I18N.getLocale());
                                calendarLink.setAsLink(false);
                                calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));
                                calendarLinks.add(calendarLink);
                            }
                        }
                    }
                }
                calendarLinksMap.put(infoRoom, calendarLinks);
            }
            return calendarLinksMap;
        } else {
            return null;
        }
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation) {
        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getExternalId().toString());
        linkParameters.put("method", "firstPage");
        return linkParameters;
    }
}
