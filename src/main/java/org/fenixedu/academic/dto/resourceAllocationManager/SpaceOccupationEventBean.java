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
package org.fenixedu.academic.dto.resourceAllocationManager;

import java.io.Serializable;

import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class SpaceOccupationEventBean implements Serializable {
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    private Space space;
    private Interval interval;
    private String description;
    private OccupationType type;

    public SpaceOccupationEventBean(Space space, Interval interval, String description, OccupationType type) {
        super();
        this.space = space;
        this.interval = interval;
        this.description = description;
        this.type = type;
    }

    public String getPresentationInterval() {
        final DateTime start = interval.getStart();
        final DateTime end = interval.getEnd();
        final DateMidnight dateMidnight = start.toDateMidnight();
        if (dateMidnight.equals(end.toDateMidnight())) {
            return String.format("%s : %s as %s", dateMidnight.toString(DATE_FORMAT), getTime(start), getTime(end));
        }
        return interval.toString();
    }

    public boolean isIntervalInSameDay() {
        return getDateDay() != null;
    }

    public DateMidnight getDateDay() {
        final DateTime start = interval.getStart();
        final DateTime end = interval.getEnd();
        final DateMidnight dateMidnight = start.toDateMidnight();
        if (dateMidnight.equals(end.toDateMidnight())) {
            return dateMidnight;
        }
        return null;
    }

    private String getTime(DateTime time) {
        return time.toString(TIME_FORMAT);
    }

    public String getStartTime() {
        return getTime(interval.getStart());
    }

    public String getEndTime() {
        return getTime(interval.getEnd());
    }

    public String getDay() {
        final DateMidnight dateDay = getDateDay();
        if (dateDay == null) {
            return String.format("%s - %s", interval.getStart().toString("dd/MM/yyyy"), interval.getEnd().toString("dd/MM/yyyy"));
        }
        return dateDay.toString("dd/MM/yyyy");
    }

    public String getSpaceName() {
        return space.getName();
    }

    public String getDescription() {
        return description;
    }

    public Interval getInterval() {
        return interval;
    }

    public Space getSpace() {
        return space;
    }

    public OccupationType getOccupationType() {
        return type;
    }

    public void setOccupationType(OccupationType type) {
        this.type = type;
    }
}
