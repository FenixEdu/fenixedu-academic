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
/*
 * Created on 24/Set/2003
 */
package org.fenixedu.academic.service.services.manager;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.InfoExecutionCourseEditor;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1 modified by Fernanda Quitério
 */
//TODO: check usage
public class InsertExecutionCourseAtExecutionPeriod {

    @Atomic
    public static void run(InfoExecutionCourseEditor infoExecutionCourse) throws FenixServiceException {

        final ExecutionInterval executionInterval =
                FenixFramework.getDomainObject(infoExecutionCourse.getInfoExecutionPeriod().getExternalId());
        if (executionInterval == null) {
            throw new DomainException("message.nonExistingExecutionPeriod");
        }

        final ExecutionCourse existentExecutionCourse =
                readBySiglaAndExecutionPeriod(infoExecutionCourse.getSigla(), executionInterval);
        if (existentExecutionCourse != null) {
            throw new DomainException("error.manager.executionCourseManagement.acronym.exists",
                    existentExecutionCourse.getSigla(), executionInterval.getName(),
                    executionInterval.getExecutionYear().getYear(), existentExecutionCourse.getName());
        }

        final ExecutionCourse executionCourse = new ExecutionCourse(infoExecutionCourse.getNome(), infoExecutionCourse.getSigla(),
                executionInterval, infoExecutionCourse.getEntryPhase());
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