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
package org.fenixedu.academic.domain;

import java.util.Collections;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.curriculum.EnrollmentCondition;
import org.fenixedu.academic.domain.enrolment.ExternalDegreeEnrolmentWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.util.EnrolmentAction;
import org.fenixedu.academic.util.MultiLanguageString;

public class ExternalDegreeEnrolment extends ExternalDegreeEnrolment_Base {

    public ExternalDegreeEnrolment() {
        super();
    }

    public ExternalDegreeEnrolment(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {

        this();
        checkParameters(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        checkInitConstraints(studentCurricularPlan, curricularCourse, executionSemester);
        initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionSemester, enrolmentCondition,
                createdBy);
        createCurriculumLineLog(EnrolmentAction.ENROL);
    }

    private void checkParameters(final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup,
            final CurricularCourse curricularCourse, final ExecutionSemester executionSemester,
            final EnrollmentCondition enrolmentCondition, final String createdBy) {

        if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null || executionSemester == null
                || enrolmentCondition == null || createdBy == null) {
            throw new DomainException("error.ExternalDegreeEnrolment.invalid.parameters");
        }
    }

    @Override
    public MultiLanguageString getName() {
        MultiLanguageString multiLanguageString = new MultiLanguageString();

        if (!StringUtils.isEmpty(this.getDegreeModule().getName())) {
            multiLanguageString =
                    multiLanguageString.with(MultiLanguageString.pt, getDegreeModule().getName() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }
        if (!StringUtils.isEmpty(this.getDegreeModule().getNameEn())) {
            multiLanguageString =
                    multiLanguageString.with(MultiLanguageString.en, getDegreeModule().getNameEn() + " ("
                            + getDegreeCurricularPlanOfDegreeModule().getName() + ")");
        }
        return multiLanguageString;
    }

    @Override
    final public StringBuilder print(String tabs) {
        final StringBuilder builder = new StringBuilder();
        builder.append(tabs);
        builder.append("[E ").append(getDegreeModule().getName()).append(" (");
        builder.append(getDegreeCurricularPlanOfDegreeModule().getName()).append(") ]\n");
        return builder;
    }

    @Override
    public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(ExecutionSemester executionSemester) {
        if (isValid(executionSemester) && isEnroled()) {
            return Collections.<IDegreeModuleToEvaluate> singleton(new ExternalDegreeEnrolmentWrapper(this, executionSemester));
        }
        return Collections.emptySet();
    }
}
