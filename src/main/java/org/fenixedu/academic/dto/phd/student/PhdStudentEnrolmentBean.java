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
package org.fenixedu.academic.dto.phd.student;

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.dto.student.enrollment.bolonha.BolonhaStudentEnrollmentBean;

public class PhdStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    static private final long serialVersionUID = 1L;

    public PhdStudentEnrolmentBean(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester,
            int[] curricularYears, CurricularRuleLevel curricularRuleLevel) {

        super(studentCurricularPlan, executionSemester, new PhdStudentCurriculumGroupBean(studentCurricularPlan.getRoot(),
                executionSemester, curricularYears), curricularRuleLevel);
    }

}
