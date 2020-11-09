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
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionCourseEditor;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditExecutionCourseInfo {

    @Atomic
    public static InfoExecutionCourse run(InfoExecutionCourseEditor infoExecutionCourse) throws InvalidArgumentsServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourse.getExternalId());
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionInterval executionInterval = executionCourse.getExecutionInterval();

        if (executionInterval == null) {
            throw new DomainException("message.nonExistingExecutionPeriod");
        }

        final ExecutionCourse existentExecutionCourse =
                readBySiglaAndExecutionPeriod(infoExecutionCourse.getSigla(), executionInterval);
        if (existentExecutionCourse != null && !existentExecutionCourse.equals(executionCourse)) {
            throw new DomainException("error.manager.executionCourseManagement.acronym.exists",
                    existentExecutionCourse.getSigla(), executionInterval.getName(),
                    executionInterval.getExecutionYear().getYear(), existentExecutionCourse.getName());
        }

        executionCourse.setNome(infoExecutionCourse.getNome());
        executionCourse.setSigla(infoExecutionCourse.getSigla());
        Signal.emit(ExecutionCourse.EDITED_SIGNAL, new DomainObjectEvent<ExecutionCourse>(executionCourse));

        return new InfoExecutionCourse(executionCourse);
    }

    private static ExecutionCourse readBySiglaAndExecutionPeriod(final String sigla, ExecutionInterval executionInterval) {
        for (ExecutionCourse executionCourse : executionInterval.getAssociatedExecutionCoursesSet()) {
            if (sigla.equalsIgnoreCase(executionCourse.getSigla())) {
                return executionCourse;
            }
        }
        return null;
    }
}