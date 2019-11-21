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

import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import org.fenixedu.bennu.core.security.Authenticate;

import com.google.common.collect.Sets;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForMoveCurriculumLines implements DataProvider {

    @Override
    public Object provide(final Object source, final Object currentValue) {

        final CurriculumLineLocationBean bean = (CurriculumLineLocationBean) source;

        final Set<CurriculumGroup> result =
                bean.isWithContextInPlan() ? provideWithContextInPlan(bean) : provideAllFromRegistrations(bean);

        return filterResults(result);
    }

    static private Set<CurriculumGroup> provideAllFromRegistrations(final CurriculumLineLocationBean bean) {
        final Set<CurriculumGroup> result = Sets.newHashSet();

        final Student student = bean.getStudent();

        for (final Registration registration : student.getRegistrationsSet()) {

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

            if (studentCurricularPlan.getCycleCurriculumGroups().isEmpty()) {
                result.addAll(studentCurricularPlan.getAllCurriculumGroups());
                continue;
            }

            result.addAll(studentCurricularPlan.getNoCourseGroupCurriculumGroups());

            for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

                if (bean.isWithRules() && isConcluded(student, cycle)) {
                    continue;
                }

                result.addAll(cycle.getAllCurriculumGroups());
            }
        }

        return result;
    }

    static protected Set<CurriculumGroup> filterResults(final Set<CurriculumGroup> result) {

        final Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_ENROLMENTS", Authenticate.getUser()));

        return result.stream().filter(i -> programs.contains(i.getDegreeCurricularPlanOfStudent().getDegree()))
                .collect(Collectors.toSet());
    }

    static private boolean isConcluded(final Student student, final CycleCurriculumGroup cycle) {
        return cycle.getConclusionProcess() != null
                || cycle.isExternal() && !student.getRegistrationsFor(cycle.getDegreeCurricularPlanOfDegreeModule()).isEmpty();
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

    static private Set<CurriculumGroup> provideWithContextInPlan(final CurriculumLineLocationBean bean) {
        final Set<CurriculumGroup> result = Sets.newHashSet();

        final StudentCurricularPlan studentCurricularPlan = bean.getCurriculumLine().getStudentCurricularPlan();

        for (final CurriculumGroup curriculumGroup : studentCurricularPlan.getAllCurriculumGroups()) {

            if (curriculumGroup == bean.getCurriculumLine().getCurriculumGroup()
                    || curriculumGroup.isInternalCreditsSourceGroup()) {
                continue;
            }

            if (curriculumGroup.canAdd(bean.getCurriculumLine())) {
                result.add(curriculumGroup);
            }
        }

        return result;
    }

}
