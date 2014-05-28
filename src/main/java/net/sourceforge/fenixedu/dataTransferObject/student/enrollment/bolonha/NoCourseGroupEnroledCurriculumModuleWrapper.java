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
package net.sourceforge.fenixedu.dataTransferObject.student.enrollment.bolonha;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.CurricularRule;
import net.sourceforge.fenixedu.domain.curricularRules.ICurricularRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.EnroledCurriculumModuleWrapper;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;

public class NoCourseGroupEnroledCurriculumModuleWrapper extends EnroledCurriculumModuleWrapper {

    private static final long serialVersionUID = 1L;

    public NoCourseGroupEnroledCurriculumModuleWrapper(final CurriculumModule curriculumModule,
            final ExecutionSemester executionSemester) {
        super(curriculumModule, executionSemester);
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
    public double getAccumulatedEctsCredits(ExecutionSemester executionSemester) {
        if (getCurriculumModule().isEnrolment()) {
            final Enrolment enrolment = (Enrolment) getCurriculumModule();

            if (!enrolment.isBolonhaDegree()) {
                return enrolment.getAccumulatedEctsCredits(executionSemester);
            } else {
                return enrolment.getStudentCurricularPlan().getAccumulatedEctsCredits(getExecutionPeriod(),
                        enrolment.getCurricularCourse());
            }
        } else {
            return 0d;
        }
    }

    @Override
    public Set<ICurricularRule> getCurricularRulesFromCurriculumGroup(ExecutionSemester executionSemester) {
        return Collections.emptySet();
    }

    @Override
    public List<CurricularRule> getCurricularRulesFromDegreeModule(ExecutionSemester executionSemester) {
        return Collections.emptyList();
    }

    @Override
    public boolean isDissertation() {
        return getCurriculumModule().getDegreeModule().isDissertation();
    }

}
