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

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import net.sourceforge.fenixedu.util.Bundle;

import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ImprovementBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    static private final long serialVersionUID = 3655858704185977193L;

    public ImprovementBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
            final ExecutionSemester executionSemester) {
        super(studentCurricularPlan, executionSemester, createBean(studentCurricularPlan, executionSemester),
                CurricularRuleLevel.IMPROVEMENT_ENROLMENT);
    }

    private static ImprovementStudentCurriculumGroupBean createBean(StudentCurricularPlan scp, ExecutionSemester semester) {
        if (scp.isEmptyDegree()) {
            return new EmptyDegreeImprovementStudentCurriculumGroupBean(scp.getRoot(), semester);
        }
        return new ImprovementStudentCurriculumGroupBean(scp.getRoot(), semester);
    }

    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    @Override
    public String getFuncionalityTitle() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.improvement.enrolment");
    }

}
