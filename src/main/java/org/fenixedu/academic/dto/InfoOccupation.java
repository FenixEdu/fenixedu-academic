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
package org.fenixedu.academic.dto;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.ShiftType;
import org.fenixedu.academic.util.DayType;
import org.fenixedu.academic.util.DiaSemana;
import org.fenixedu.academic.util.MultiLanguageString;
import org.fenixedu.academic.util.renderer.GanttDiagramEvent;
import org.fenixedu.commons.i18n.I18N;
import org.fenixedu.spaces.domain.occupation.Occupation;
import org.joda.time.Interval;

public class InfoOccupation extends InfoShowOccupation implements GanttDiagramEvent {

    Interval interval;
    Occupation occupation;

    public InfoOccupation(Occupation occupation, Interval interval) {
        this.occupation = occupation;
        this.interval = interval;
    }

    @Override
    public InfoShift getInfoShift() {
        return null;
    }

    @Override
    public ShiftType getTipo() {
        return null;
    }

    @Override
    public InfoRoomOccupation getInfoRoomOccupation() {
        return null;
    }

    @Override
    public DiaSemana getDiaSemana() {
        return DiaSemana.fromJodaWeekDay(interval.getStart().getDayOfWeek());
    }

    @Override
    public Calendar getInicio() {
        return interval.getStart().toCalendar(I18N.getLocale());
    }

    @Override
    public Calendar getFim() {
        return interval.getEnd().toCalendar(I18N.getLocale());
    }

    @Override
    public List<Interval> getGanttDiagramEventSortedIntervals() {
        return occupation.getIntervals();
    }

    @Override
    public MultiLanguageString getGanttDiagramEventName() {
        return new MultiLanguageString(occupation.getSubject());
    }

    @Override
    public int getGanttDiagramEventOffset() {
        return 0;
    }

    @Override
    public String getGanttDiagramEventPeriod() {
        return occupation.getSummary();
    }

    @Override
    public String getGanttDiagramEventObservations() {
        return occupation.getSpaces().stream().map(space -> space.getName()).collect(Collectors.joining(" "));
    }

    @Override
    public String getGanttDiagramEventIdentifier() {
        return occupation.getRequest().getExternalId();
    }

    @Override
    public Integer getGanttDiagramEventMonth() {
        return interval.getStart().getMonthOfYear();
    }

    @Override
    public String getGanttDiagramEventUrlAddOns() {
        return null;
    }

    @Override
    public boolean isGanttDiagramEventIntervalsLongerThanOneDay() {
        return false;
    }

    @Override
    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays() {
        return false;
    }

    @Override
    public DayType getGanttDiagramEventDayType(Interval interval) {
        return null;
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public String getTitle() {
        return occupation.getSubject();
    }

    public String getDescription() {
        return occupation.getDescription();
    }

    @Override
    public String getExternalId() {
        return occupation.getExternalId();
    }
}