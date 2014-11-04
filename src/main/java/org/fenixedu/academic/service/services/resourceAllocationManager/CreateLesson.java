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
 *
 * Created on 2003/08/12
 */
package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomOccupationEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.DiaSemana;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.YearMonthDay;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateLesson {

    @Atomic
    public static void run(DiaSemana weekDay, Calendar begin, Calendar end, FrequencyType frequency,
            InfoRoomOccupationEditor infoRoomOccupation, InfoShift infoShift, YearMonthDay beginDate, YearMonthDay endDate)
            throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);

        final ExecutionSemester executionSemester =
                FenixFramework.getDomainObject(infoShift.getInfoDisciplinaExecucao().getInfoExecutionPeriod().getExternalId());

        final Shift shift = FenixFramework.getDomainObject(infoShift.getExternalId());

        Space room = null;
        if (infoRoomOccupation != null) {
            room =
                    infoRoomOccupation.getInfoRoom() != null ? SpaceUtils
                            .findAllocatableSpaceForEducationByName(infoRoomOccupation.getInfoRoom().getNome()) : null;
        }

        new Lesson(weekDay, begin, end, shift, frequency, executionSemester, beginDate, endDate, room);
    }
}