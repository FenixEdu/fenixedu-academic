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
package org.fenixedu.academic.domain.util.icalendar;

import java.util.Set;

import org.fenixedu.academic.domain.Shift;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

public class ClassEventBean extends EventBean {

    private Shift classShift;

    public ClassEventBean(DateTime begin, DateTime end, boolean allDay, Set<Space> rooms, String url, String note,
            Shift classShift) {
        super(null, begin, end, allDay, rooms, url, note);
        setClassShift(classShift);
    }

    public Shift getClassShift() {
        return classShift;
    }

    public void setClassShift(Shift classShift) {
        this.classShift = classShift;
    }

    @Override
    public String getTitle() {
        return getClassShift().getExecutionCourse().getNome() + " : " + getClassShift().getShiftTypesCapitalizedPrettyPrint();
    }

}
