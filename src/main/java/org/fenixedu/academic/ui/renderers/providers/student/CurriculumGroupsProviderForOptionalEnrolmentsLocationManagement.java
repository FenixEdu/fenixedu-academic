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

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.dto.student.OptionalCurricularCoursesLocationBean.OptionalEnrolmentLocationBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForOptionalEnrolmentsLocationManagement implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        final OptionalEnrolmentLocationBean bean = (OptionalEnrolmentLocationBean) source;

        final Collection<CurriculumGroup> result =
                new TreeSet<CurriculumGroup>(CurriculumGroup.COMPARATOR_BY_FULL_PATH_NAME_AND_ID);

        final Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(PermissionService.getDegrees("ACADEMIC_OFFICE_ENROLMENTS", Authenticate.getUser()));

        for (final Registration registration : bean.getStudent().getRegistrationsSet()) {

            if (!registration.isBolonha()) {
                continue;
            }

            if (!programs.contains(registration.getDegree())) {
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            result.addAll(studentCurricularPlan.getCurricularCoursePossibleGroups(bean.getEnrolment().getCurricularCourse()));
        }
        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
