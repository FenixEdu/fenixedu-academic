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

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.EnrolmentAction;

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

    @Deprecated
    public boolean hasOptionalCurricularCourse() {
        return getOptionalCurricularCourse() != null;
    }

}
