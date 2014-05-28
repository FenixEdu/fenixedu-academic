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
package net.sourceforge.fenixedu.domain.enrolment;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;

public class ExternalDegreeEnrolmentWrapper extends EnroledEnrolmentWrapper {

    public ExternalDegreeEnrolmentWrapper(final Enrolment enrolment, final ExecutionSemester executionSemester) {
        super(enrolment, executionSemester);
    }

    @Override
    public Context getContext() {
        // does not have any connection to student's curricular plan
        return null;
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(final ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }
}
