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

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.degreeStructure.DegreeModule;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.groups.PermissionService;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.studentCurriculum.CycleCurriculumGroup;
import org.fenixedu.academic.dto.student.OptionalCurricularCoursesLocationBean.EnrolmentLocationBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class CurriculumGroupsProviderForEnrolmentsLocationManagement implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {

        final EnrolmentLocationBean bean = (EnrolmentLocationBean) source;
        final Set<DegreeModule> result = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);

        final Set<AcademicProgram> programs = AcademicAccessRule
                .getProgramsAccessibleToFunction(AcademicOperationType.STUDENT_ENROLMENTS, Authenticate.getUser())
                .collect(Collectors.toSet());
        programs.addAll(PermissionService.getObjects("ACADEMIC_OFFICE_ENROLMENTS", Degree.class, Authenticate.getUser()));

        for (final Registration registration : bean.getStudent().getRegistrationsSet()) {

            if (!registration.isBolonha()) {
                continue;
            }

            if (!programs.contains(registration.getDegree())) {
                continue;
            }

            final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();
            for (final CycleCurriculumGroup cycle : studentCurricularPlan.getCycleCurriculumGroups()) {

                if (cycle.getConclusionProcess() != null) {
                    continue;
                }

                final DegreeCurricularPlan degreeCurricularPlan = cycle.getDegreeCurricularPlanOfDegreeModule();
                final List<DegreeModule> modules = degreeCurricularPlan.getDcpDegreeModules(OptionalCurricularCourse.class,
                        ExecutionYear.findCurrent(degreeCurricularPlan.getDegree().getCalendar()));

                final Iterator<DegreeModule> degreeModulesIter = modules.iterator();
                while (degreeModulesIter.hasNext()) {
                    final CurricularCourse curricularCourse = (CurricularCourse) degreeModulesIter.next();
                    if (studentCurricularPlan.isApproved(curricularCourse) || studentCurricularPlan
                            .getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(curricularCourse).isEmpty()) {
                        degreeModulesIter.remove();
                    }
                }

                result.addAll(modules);
            }
        }

        return result;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
