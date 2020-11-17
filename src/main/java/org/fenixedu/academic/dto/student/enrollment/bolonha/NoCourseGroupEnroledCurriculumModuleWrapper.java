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
package org.fenixedu.academic.dto.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.curricularRules.CurricularRule;
import org.fenixedu.academic.domain.curricularRules.ICurricularRule;
import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.domain.enrolment.EnroledCurriculumModuleWrapper;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumModule;

public class NoCourseGroupEnroledCurriculumModuleWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 1L;

    public NoCourseGroupEnroledCurriculumModuleWrapper(final CurriculumModule curriculumModule,
            final ExecutionInterval executionInterval) {
        super(curriculumModule, executionInterval);
    }

    @Override
    public boolean canCollectRules() {
        return false;
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public double getAccumulatedEctsCredits(ExecutionInterval executionInterval) {
        if (getCurriculumModule().isEnrolment()) {
            final Enrolment enrolment = (Enrolment) getCurriculumModule();
            return enrolment.getStudentCurricularPlan().getAccumulatedEctsCredits(getExecutionInterval(),
                    enrolment.getCurricularCourse());
        } else {
            return 0d;
        }
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionInterval executionInterval) {
        return Collections.emptySet();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionInterval executionInterval) {
        return Collections.emptyList();
    }

    @Override
    public boolean isDissertation() {
        return getCurriculumModule().getDegreeModule().isDissertation();
    }

}
