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
package net.sourceforge.fenixedu.util.renderer;

import java.util.List;

import net.sourceforge.fenixedu.util.DayType;

import org.joda.time.Interval;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public interface GanttDiagramEvent {

    public List<Interval> getGanttDiagramEventSortedIntervals();

    public MultiLanguageString getGanttDiagramEventName();

    public int getGanttDiagramEventOffset();

    public String getGanttDiagramEventPeriod();

    public String getGanttDiagramEventObservations();

    public String getGanttDiagramEventIdentifier();

    public Integer getGanttDiagramEventMonth();

    public String getGanttDiagramEventUrlAddOns();

    public boolean isGanttDiagramEventIntervalsLongerThanOneDay();

    public boolean isGanttDiagramEventToMarkWeekendsAndHolidays();

    public DayType getGanttDiagramEventDayType(Interval interval);

}
