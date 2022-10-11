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

import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

@SuppressWarnings("serial")
public class ImprovementBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    private EvaluationSeason evaluationSeason;

    public ImprovementBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionInterval executionSemester, final EvaluationSeason evaluationSeason) {
        super(studentCurricularPlan, executionSemester, createBean(studentCurricularPlan, executionSemester, evaluationSeason),
                CurricularRuleLevel.IMPROVEMENT_ENROLMENT);
        setEvaluationSeason(evaluationSeason);
    }

    private static StudentCurriculumGroupBean createBean(final StudentCurricularPlan scp, final ExecutionInterval interval,
            final EvaluationSeason evaluationSeason) {
        return ImprovementStudentCurriculumGroupBean.create(scp.getRoot(), interval, evaluationSeason);
    }

    @Override
    public String getFuncionalityTitle() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.improvement.enrolment");
    }

    public EvaluationSeason getEvaluationSeason() {
        return evaluationSeason;
    }

    public void setEvaluationSeason(EvaluationSeason evaluationSeason) {
        this.evaluationSeason = evaluationSeason;
    }

}
