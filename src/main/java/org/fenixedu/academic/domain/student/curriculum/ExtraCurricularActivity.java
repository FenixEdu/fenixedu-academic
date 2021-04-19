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
package org.fenixedu.academic.domain.student.curriculum;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.Interval;
import org.joda.time.Partial;

import pt.ist.fenixframework.Atomic;

/**
 * Extra curricular activity performed by a student during some period of time.
 * 
 * This information is printed in the student's diploma supplement. The set of
 * acceptable activities is defined by a candidacy process managed by the
 * directive council, the students that are accounted to have had a particular
 * activity are defined by someone responsible in that activity.
 * 
 * @author Pedro Santos (pedro.miguel.santos@ist.utl.pt)
 */
public class ExtraCurricularActivity extends ExtraCurricularActivity_Base {

    public ExtraCurricularActivity(Student student, ExtraCurricularActivityType type, Interval interval) {
        super();
        checkParameters(student, type, interval);
        setRootDomainObject(Bennu.getInstance());
        setStudent(student);
        setType(type);
        setActivityInterval(interval);
    }

    public ExtraCurricularActivity(Student student, ExtraCurricularActivityType type, Partial start, Partial end) {
        this(student, type, new Interval(start.toDateTime(new DateTime(0)), end.toDateTime(new DateTime(0))));
    }

    private void checkParameters(Student student, ExtraCurricularActivityType type, Interval interval) {
        Set<ExtraCurricularActivityType> existing = new HashSet<ExtraCurricularActivityType>();
        for (ExtraCurricularActivity activity : student.getExtraCurricularActivitySet()) {
            existing.add(activity.getType());
            if (activity.getType().equals(type) && activity.getActivityInterval().overlaps(interval)) {
                throw new DomainException("error.extraCurricularActivity.overlaping");
            }
        }
    }
    
    public void edit(Interval interval) {
        checkParameters(getStudent(), getType(), interval);
        super.setActivityInterval(interval);
    }

    public boolean getIsDeletable() {
        return true;
    }

    @Override
    public void setStudent(Student student) {
        if (!getIsDeletable()) {
            throw new DomainException("information.already.featured.in.official.document");
        }
        super.setStudent(student);
    }

    @Override
    public void setType(ExtraCurricularActivityType type) {
        if (!getIsDeletable()) {
            throw new DomainException("information.already.featured.in.official.document");
        }
        super.setType(type);
    }

    @Override
    public void setActivityInterval(Interval interval) {
        if (!getIsDeletable()) {
            throw new DomainException("information.already.featured.in.official.document");
        }
        super.setActivityInterval(interval);
    }

    public Partial getStart() {
        DateTime start = getActivityInterval().getStart();
        return new Partial().with(DateTimeFieldType.year(), start.getYear()).with(DateTimeFieldType.monthOfYear(),
                start.getMonthOfYear());
    }

    public void setStart(Partial start) {
        setActivityInterval(new Interval(start.toDateTime(new DateTime(0)), getActivityInterval().getEnd()));
    }

    public Partial getEnd() {
        DateTime end = getActivityInterval().getEnd();
        return new Partial().with(DateTimeFieldType.year(), end.getYear()).with(DateTimeFieldType.monthOfYear(),
                end.getMonthOfYear());
    }

    public void setEnd(Partial end) {
        setActivityInterval(new Interval(getActivityInterval().getStart(), end.toDateTime(new DateTime(0))));
    }

    @Atomic
    public void delete() {
        if (!getIsDeletable()) {
            throw new DomainException("information.already.featured.in.official.document");
        }
        setType(null);
        setStudent(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

}
