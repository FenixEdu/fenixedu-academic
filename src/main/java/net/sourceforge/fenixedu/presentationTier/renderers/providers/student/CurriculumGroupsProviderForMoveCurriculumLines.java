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
package net.sourceforge.fenixedu.presentationTier.renderers.providers.student;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.AcademicProgram;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
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

        for (final Registration registration : student.getRegistrations()) {

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
                AcademicAuthorizationGroup.getProgramsForOperation(AccessControl.getPerson(), AcademicOperationType.STUDENT_ENROLMENTS);

        return Collections2.filter(result, new Predicate<CurriculumGroup>() {
            @Override
            public boolean apply(CurriculumGroup group) {

                return programs.contains(group.getDegreeCurricularPlanOfStudent().getDegree());
            }
        });
    }

    private boolean isConcluded(final Student student, final CycleCurriculumGroup cycle) {
        return cycle.hasConclusionProcess()
                || (cycle.isExternal() && student.hasRegistrationFor(cycle.getDegreeCurricularPlanOfDegreeModule()));
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyConverter();
    }

}
