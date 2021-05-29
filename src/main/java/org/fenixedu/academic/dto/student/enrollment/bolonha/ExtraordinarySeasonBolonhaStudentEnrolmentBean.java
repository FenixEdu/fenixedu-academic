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

import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import org.fenixedu.academic.ui.renderers.converters.CurriculumModuleEnroledWrapperConverter;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class ExtraordinarySeasonBolonhaStudentEnrolmentBean extends BolonhaStudentEnrollmentBean {

    private static final long serialVersionUID = -7472651937511355140L;

    public ExtraordinarySeasonBolonhaStudentEnrolmentBean(final StudentCurricularPlan studentCurricularPlan,
                                                          final ExecutionSemester executionSemester) {
        super(studentCurricularPlan, executionSemester, new ExtraordinarySeasonStudentCurriculumGroupBean(
                studentCurricularPlan.getRoot(), executionSemester), CurricularRuleLevel.EXTRAORDINARY_SEASON_ENROLMENT);
    }

    @Override
    public Converter getDegreeModulesToEvaluateConverter() {
        return new CurriculumModuleEnroledWrapperConverter();
    }

    @Override
    public String getFuncionalityTitle() {
        return BundleUtil.getString(Bundle.ACADEMIC, "label.extraordinary.season.enrolment");
    }

}
