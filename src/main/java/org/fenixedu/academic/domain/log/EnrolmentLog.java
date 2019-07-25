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
/*
 * Created on Nov 4, 2004
 */
package org.fenixedu.academic.domain.log;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.EnrolmentAction;

/**
 * @author nmgo
 * @author lmre
 */
public class EnrolmentLog extends EnrolmentLog_Base {

    protected EnrolmentLog() {
        super();
    }

    public EnrolmentLog(final EnrolmentAction action, final Registration registration, final CurricularCourse curricularCourse,
            final ExecutionInterval executionInterval, final String who) {
        this();
        init(action, registration, curricularCourse, executionInterval, who);
    }

    @Override
    public CurricularCourse getDegreeModule() {
        return (CurricularCourse) super.getDegreeModule();
    }

    @Override
    public String getDescription() {
        return getDegreeModule().getName(getExecutionPeriod());
    }

}
