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
package org.fenixedu.academic.ui.spring.controller.teacher.professorship;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.Teacher;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.User;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

import com.google.common.collect.ImmutableList;

@Service
public class ProfessorshipService {

    public List<Professorship> getProfessorships(User user, ExecutionSemester period) {
        Teacher teacher = user.getPerson().getTeacher();
        if (teacher == null) {
            return ImmutableList.<Professorship> builder().build();
        }
        return teacher.getProfessorships(period).stream().collect(Collectors.toList());
    }

    public String getDegreeAcronyms(Professorship professorship, String separator) {
        return professorship.getExecutionCourse().getExecutionDegrees().stream().map(ExecutionDegree::getDegree)
                .map(Degree::getSigla).distinct().collect(Collectors.joining(separator));
    }

    @Atomic(mode = TxMode.WRITE)
    public Boolean changeResponsibleFor(Professorship professorship, Boolean responsibleFor) {
        if (responsibleFor != null && !professorship.getResponsibleFor().equals(responsibleFor)) {
            professorship.setResponsibleFor(responsibleFor);
        }

        return professorship.getResponsibleFor();
    }

    public List<ExecutionDegree> getDegrees(ExecutionSemester period) {
        if (period == null) {
            return ImmutableList.<ExecutionDegree> builder().build();
        }
        return period.getExecutionYear().getExecutionDegreesSet().stream()
                .sorted(ExecutionDegree.EXECUTION_DEGREE_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).distinct()
                .collect(Collectors.toList());
    }

    public List<ExecutionCourse> getCourses(ExecutionDegree executionDegree, ExecutionSemester period) {
        if (executionDegree == null) {
            return ImmutableList.<ExecutionCourse> builder().build();
        }
        return executionDegree.getDegreeCurricularPlan().getExecutionCoursesByExecutionPeriod(period).stream().distinct()
                .sorted(ExecutionCourse.EXECUTION_COURSE_NAME_COMPARATOR).collect(Collectors.toList());
    }

    public Professorship create(CreateProfessorshipBean bean) throws DomainException {
        return Professorship.create(bean.getResponsibleFor(), bean.getCourse(), bean.getTeacher());
    }

    @Atomic(mode = TxMode.WRITE)
    public void deleteProfessorship(Professorship professorship) throws DomainException {
        professorship.delete();
    }
}
