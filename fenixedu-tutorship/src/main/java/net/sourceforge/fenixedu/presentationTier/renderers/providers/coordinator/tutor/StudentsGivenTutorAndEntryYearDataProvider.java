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
package org.fenixedu.academic.ui.renderers.providers.coordinator.tutor;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.dto.coordinator.tutor.TutorshipManagementByEntryYearBean;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.Tutorship;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.DataProvider;
import pt.ist.fenixWebFramework.renderers.components.converters.Converter;

public class StudentsGivenTutorAndEntryYearDataProvider implements DataProvider {

    @Override
    public Object provide(Object source, Object currentValue) {
        TutorshipManagementByEntryYearBean bean = (TutorshipManagementByEntryYearBean) source;

        Teacher teacher = bean.getTeacher();

        List<Tutorship> tutorships = new ArrayList<Tutorship>();

        for (Tutorship tutorship : teacher.getTutorshipsSet()) {
            StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
            ExecutionYear studentEntryYear =
                    ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
            if (studentEntryYear.equals(bean.getExecutionYear()) && tutorship.isActive()) {
                tutorships.add(tutorship);
            }
        }

        return tutorships;
    }

    @Override
    public Converter getConverter() {
        return new DomainObjectKeyArrayConverter();
    }
}
