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
package org.fenixedu.academic.ui.struts.action.phd.coordinator.providers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.AcademicProgram;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EnrolmentPeriodInCurricularCourses;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicAccessRule;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.phd.ManageEnrolmentsBean;
import org.fenixedu.bennu.core.security.Authenticate;

import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class PhdDegreeCurricularPlansProvider implements DataProvider {

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }

    @Override
    public Object provide(Object source, Object currentValue) {

        Set<AcademicProgram> programs =
                AcademicAccessRule.getProgramsAccessibleToFunction(AcademicOperationType.MANAGE_PHD_ENROLMENT_PERIODS,
                        Authenticate.getUser()).collect(Collectors.toSet());

        final ManageEnrolmentsBean bean = (ManageEnrolmentsBean) source;

        final List<DegreeCurricularPlan> result = new ArrayList<DegreeCurricularPlan>();

        for (final ExecutionDegree executionDegree : bean.getSemester().getExecutionYear()
                .getExecutionDegreesMatching(DegreeType::isAdvancedSpecializationDiploma)) {

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

            if (!hasEnrolmentPeriod(degreeCurricularPlan, bean.getSemester())
                    && programs.contains(degreeCurricularPlan.getDegree().getPhdProgram())) {
                result.add(degreeCurricularPlan);
            }

        }
        return result;
    }

    private boolean hasEnrolmentPeriod(DegreeCurricularPlan degreeCurricularPlan, ExecutionSemester semester) {
        return semester.getEnrolmentPeriod(EnrolmentPeriodInCurricularCourses.class, degreeCurricularPlan) != null;
    }
}
