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

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.EnrolmentAction;

public class OptionalEnrolmentLog extends OptionalEnrolmentLog_Base {

    private OptionalEnrolmentLog() {
        super();
    }

    public OptionalEnrolmentLog(final EnrolmentAction action, final Registration registration,
            final CurricularCourse curricularCourse, final OptionalCurricularCourse optionalCurricularCourse,
            final ExecutionSemester executionSemester, final String who) {

        this();
        String[] args = {};
        if (optionalCurricularCourse == null) {
            throw new DomainException("error.OptionalEnrolmentLog.invalid.optionalCurricularCourse", args);
        }
        init(action, registration, curricularCourse, executionSemester, who);
        setOptionalCurricularCourse(optionalCurricularCourse);
    }

    @Override
    protected void disconnect() {
        setOptionalCurricularCourse(null);
        super.disconnect();
    }

    @Override
    public String getDescription() {
        return getOptionalCurricularCourse().getName(getExecutionPeriod()) + " (" + super.getDescription() + ")";
    }

}
