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
package org.fenixedu.academic.domain.studentCurriculum;

import java.util.Collection;
import java.util.Collections;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.student.curriculum.ICurriculumEntry;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;

public class TemporarySubstitution extends TemporarySubstitution_Base {

    public TemporarySubstitution() {
        super();
    }

    public TemporarySubstitution(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    @Override
    public boolean isTemporary() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ICurriculumEntry> getAverageEntries(final ExecutionYear executionYear) {
        return Collections.EMPTY_SET;
    }

}
