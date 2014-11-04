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
package org.fenixedu.academic.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;

import org.joda.time.DateTime;

public class TutorshipIntention extends TutorshipIntention_Base {

    static final public Comparator<TutorshipIntention> COMPARATOR_FOR_ATTRIBUTING_TUTOR_STUDENTS =
            new Comparator<TutorshipIntention>() {

                @Override
                public int compare(TutorshipIntention ti1, TutorshipIntention ti2) {
                    return ti1.getTeacher().getPerson().getName().compareTo(ti2.getTeacher().getPerson().getName());
                }
            };

    public TutorshipIntention(DegreeCurricularPlan dcp, Teacher teacher, AcademicInterval interval) {
        super();
        setDegreeCurricularPlan(dcp);
        setTeacher(teacher);
        setAcademicInterval(interval);
        checkOverlaps();
    }

    private void checkOverlaps() {
        for (TutorshipIntention intention : getDegreeCurricularPlan().getTutorshipIntentionSet()) {
            if (!intention.equals(this) && intention.getTeacher().equals(getTeacher())
                    && intention.getAcademicInterval().overlaps(getAcademicInterval())) {
                throw new DomainException("error.tutorship.overlapingTutorshipIntentions");
            }
        }
    }

    public boolean isDeletable() {
        for (Tutorship tutorship : getTeacher().getTutorshipsSet()) {
            if (tutorship.getStudentCurricularPlan().getDegreeCurricularPlan().equals(getDegreeCurricularPlan())
                    && getAcademicInterval().contains(tutorship.getStartDate().toDateTime(new DateTime(0)))) {
                return false;
            }
        }
        return true;
    }

    public List<Tutorship> getTutorships() {
        List<Tutorship> result = new ArrayList<Tutorship>();
        for (Tutorship tutorship : getTeacher().getTutorshipsSet()) {
            if (tutorship.getStudentCurricularPlan().getDegreeCurricularPlan().equals(getDegreeCurricularPlan())
                    && getAcademicInterval().contains(tutorship.getStartDate().toDateTime(new DateTime(0)))) {
                result.add(tutorship);
            }
        }
        return result;
    }

    public void delete() {
        setDegreeCurricularPlan(null);
        setTeacher(null);
        deleteDomainObject();
    }

    public static TutorshipIntention readByDcpAndTeacherAndInterval(DegreeCurricularPlan dcp, Teacher teacher,
            AcademicInterval academicInterval) {
        for (TutorshipIntention intention : teacher.getTutorshipIntentionSet()) {
            if (intention.getDegreeCurricularPlan().equals(dcp) && intention.getAcademicInterval().equals(academicInterval)) {
                return intention;
            }
        }
        return null;
    }

    public static List<TutorshipIntention> getTutorshipIntentions(ExecutionDegree executionDegree) {
        List<TutorshipIntention> result = new ArrayList<TutorshipIntention>();
        for (TutorshipIntention tutorshipIntention : executionDegree.getDegreeCurricularPlan().getTutorshipIntentionSet()) {
            if (tutorshipIntention.getAcademicInterval().equals(executionDegree.getAcademicInterval())) {
                result.add(tutorshipIntention);
            }
        }
        Collections.sort(result, TutorshipIntention.COMPARATOR_FOR_ATTRIBUTING_TUTOR_STUDENTS);
        return result;
    }

}
