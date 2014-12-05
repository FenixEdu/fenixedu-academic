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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Calendar;

import org.fenixedu.academic.domain.FrequencyType;
import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.space.SpaceUtils;
import org.fenixedu.academic.dto.InfoLesson;
import org.fenixedu.academic.dto.InfoRoomOccupationEditor;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditLesson {

    @Atomic
    public static Lesson run(InfoLesson aulaAntiga, DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
            InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay newBeginDate, YearMonthDay newEndDate,
            Boolean createLessonInstances) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        Lesson lesson = FenixFramework.getDomainObject(aulaAntiga.getExternalId());

        if (lesson != null) {

            Space newRoom = null;
            if (infoRoomOccupation != null && infoRoomOccupation.getInfoRoom() != null) {
                newRoom = SpaceUtils.findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome());
            }

            lesson.edit(newBeginDate, newEndDate, weekDay, begin, end, frequency, createLessonInstances, newRoom);
        }
        return lesson;
    }

    @Atomic
    public static void run(final Lesson lesson, final Space space) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        lesson.edit(space);
    }

}