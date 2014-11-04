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
package org.fenixedu.academic.domain.teacher.evaluation;

import java.util.Comparator;

import org.fenixedu.bennu.core.domain.Bennu;

public class ApprovedTeacherEvaluationProcessMark extends ApprovedTeacherEvaluationProcessMark_Base {

    public static final Comparator<ApprovedTeacherEvaluationProcessMark> COMPARATOR_BY_YEAR =
            new Comparator<ApprovedTeacherEvaluationProcessMark>() {
                @Override
                public int compare(final ApprovedTeacherEvaluationProcessMark o1, final ApprovedTeacherEvaluationProcessMark o2) {
                    final String year1 = o1.getFacultyEvaluationProcessYear().getYear();
                    final String year2 = o2.getFacultyEvaluationProcessYear().getYear();
                    return year1.compareTo(year2);
                }
            };

    public ApprovedTeacherEvaluationProcessMark(final FacultyEvaluationProcessYear facultyEvaluationProcessYear,
            final TeacherEvaluationProcess teacherEvaluationProcess) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setFacultyEvaluationProcessYear(facultyEvaluationProcessYear);
        setTeacherEvaluationProces(teacherEvaluationProcess);
    }

    public void delete() {
        setFacultyEvaluationProcessYear(null);
        setTeacherEvaluationProces(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

}
