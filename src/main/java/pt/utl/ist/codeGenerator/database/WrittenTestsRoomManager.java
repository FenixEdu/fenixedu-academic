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
package pt.utl.ist.codeGenerator.database;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.FrequencyType;
import net.sourceforge.fenixedu.domain.space.SpaceUtils;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.HourMinuteSecond;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

public class WrittenTestsRoomManager extends HashSet<Space> {

    private final Map<ExecutionSemester, EvaluationRoomManager> evaluationRoomManagerMap =
            new HashMap<ExecutionSemester, EvaluationRoomManager>();

    public DateTime getNextDateTime(final ExecutionSemester executionPeriod) {
        EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
        if (evaluationRoomManager == null) {
            evaluationRoomManager =
                    new EvaluationRoomManager(executionPeriod.getBeginDateYearMonthDay().plusMonths(1).toDateTimeAtMidnight(),
                            executionPeriod.getEndDateYearMonthDay().minusDays(31).toDateTimeAtMidnight(), 120, this);
            evaluationRoomManagerMap.put(executionPeriod, evaluationRoomManager);
        }

        DateTime dateTime;
        Space oldRoom;

        do {
            dateTime = evaluationRoomManager.getNextDateTime();
            oldRoom = evaluationRoomManager.getNextOldRoom();

        } while (SpaceUtils
                .isFree(oldRoom, dateTime.toYearMonthDay(), dateTime.plusMinutes(120).toYearMonthDay(), new HourMinuteSecond(
                        dateTime.getHourOfDay(), dateTime.getMinuteOfHour(), dateTime.getSecondOfMinute()),
                        dateTime.plusMinutes(120).getHourOfDay() == 0 ? new HourMinuteSecond(dateTime.plusMinutes(119)
                                .getHourOfDay(), dateTime.plusMinutes(119).getMinuteOfHour(), dateTime.plusMinutes(119)
                                .getSecondOfMinute()) : new HourMinuteSecond(dateTime.plusMinutes(120).getHourOfDay(), dateTime
                                .plusMinutes(120).getMinuteOfHour(), dateTime.plusMinutes(120).getSecondOfMinute()),
                        new DiaSemana(dateTime.getDayOfWeek() + 1), FrequencyType.DAILY, Boolean.TRUE, Boolean.TRUE));
        return dateTime;
    }

    public Space getNextOldRoom(final ExecutionSemester executionPeriod) {
        final EvaluationRoomManager evaluationRoomManager = evaluationRoomManagerMap.get(executionPeriod);
        return evaluationRoomManager.getNextOldRoom();
    }
}
