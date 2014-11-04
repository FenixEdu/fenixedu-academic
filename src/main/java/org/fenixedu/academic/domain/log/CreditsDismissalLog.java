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
package org.fenixedu.academic.domain.log;

import java.math.BigDecimal;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.Credits;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.util.EnrolmentAction;

public class CreditsDismissalLog extends CreditsDismissalLog_Base {

    private CreditsDismissalLog() {
        super();
    }

    public CreditsDismissalLog(final EnrolmentAction action, final Registration registration,
            final CurriculumGroup curriculumGroup, final Credits credits, final ExecutionSemester executionSemester,
            final String who) {
        this();

        final CourseGroup courseGroup = findCourseGroup(curriculumGroup);
        String[] args = {};
        if (courseGroup == null) {
            throw new DomainException("error.CreditsDismissalLog.invalid.courseGroup", args);
        }

        init(action, registration, courseGroup, executionSemester, who);
        setCredits(BigDecimal.valueOf(credits.getGivenCredits()));
        setSourceDescription(buildSourceDescription(credits));
    }

    private CourseGroup findCourseGroup(final CurriculumGroup curriculumGroup) {
        return curriculumGroup.isNoCourseGroupCurriculumGroup() ? curriculumGroup.getCurriculumGroup().getDegreeModule() : curriculumGroup
                .getDegreeModule();
    }

    @Override
    protected String getDegreeModuleName() {
        return getDegreeModule().getName();
    }

}
