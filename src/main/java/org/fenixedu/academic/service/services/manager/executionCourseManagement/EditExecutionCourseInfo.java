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
package org.fenixedu.academic.service.services.manager.executionCourseManagement;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionCourseEditor;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditExecutionCourseInfo {

    @Atomic
    public static InfoExecutionCourse run(InfoExecutionCourseEditor infoExecutionCourse) throws InvalidArgumentsServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();

        if (executionSemester == null) {
            throw new DomainException("message.nonExistingExecutionPeriod");
        }

        final ExecutionCourse existentExecutionCourse =
                executionSemester.getExecutionCourseByInitials(infoExecutionCourse.getSigla());
        if (existentExecutionCourse != null && !existentExecutionCourse.equals(executionCourse)) {
            throw new DomainException("error.manager.executionCourseManagement.acronym.exists",
                    existentExecutionCourse.getSigla(), executionSemester.getName(), executionSemester.getExecutionYear()
                            .getYear(), existentExecutionCourse.getName());
        }

        executionCourse.editInformation(infoExecutionCourse.getNameI18N() , infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(),
                infoExecutionCourse.getComment(), infoExecutionCourse.getAvailableGradeSubmission(),
                infoExecutionCourse.getEntryPhase());

        return new InfoExecutionCourse(executionCourse);
    }
}