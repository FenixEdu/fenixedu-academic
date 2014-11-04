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
package org.fenixedu.academic.ui.renderers.providers.student;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

public class CurriculumGroupsProviderForMoveCurriculumLines implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final CurriculumLineLocationBean bean = (CurriculumLineLocationBean) source;

        final Student student = bean.getStudent();
        final Set<CurriculumGroup> result = new HashSet<CurriculumGroup>();

        for (final Registration registration : student.getRegistrationsSet()) {

            if (!registration.isBolonha()) {
                result.addAll(registration.getLastStudentCurricularPlan().getAllCurriculumGroups());
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            result.addAll(studentCurricularPlan.getNoCourseGroupCurriculumGroups());

            for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

                if (bean.isWithRules() && isConcluded(student, cycle)) {
                    continue;
                }

                result.addAll(cycle.getAllCurriculumGroups());
            }
        }

        final Set<AcademicProgram> programs =
                AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS,
                        Authenticate.getUser()).collect(Collectors.toSet());

        return Collections2.filter(result, new Predicate<CurriculumGroup>() {
            @Override
            public boolean apply(CurriculumGroup group) {

                return programs.contains(group.getDegreeCurricularPlanOfStudent().getDegree());
            }
        });
    }

    private boolean isConcluded(final Student student, final CycleCurriculumGroup cycle) {
        return cycle.getConclusionProcess() != null
                || (cycle.isExternal() && student.hasRegistrationFor(cycle.getDegreeCurricularPlanOfDegreeModule()));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
