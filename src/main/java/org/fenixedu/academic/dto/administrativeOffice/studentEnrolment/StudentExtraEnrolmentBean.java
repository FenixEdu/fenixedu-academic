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
package org.fenixedu.academic.dto.administrativeOffice.studentEnrolment;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;

public class StudentExtraEnrolmentBean extends StudentOptionalEnrolmentBean implements NoCourseGroupEnrolmentBean {

    static private final long serialVersionUID = 1L;

    public StudentExtraEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionInterval executionInterval) {
        setStudentCurricularPlan(studentCurricularPlan);
        setExecutionPeriod(executionInterval);
    }

    @Override
    public NoCourseGroupCurriculumGroupType getGroupType() {
        return NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR;
    }

    @Override
    public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup() {
        return getStudentCurricularPlan().getNoCourseGroupCurriculumGroup(getGroupType());
    }

    @Override
    public CurricularRuleLevel getCurricularRuleLevel() {
        return super.getCurricularRuleLevel() != null ? super.getCurricularRuleLevel() : getGroupType().getCurricularRuleLevel();
    }

}
