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
package net.sourceforge.fenixedu.domain.log;

import java.math.BigDecimal;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.EnrolmentAction;

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
