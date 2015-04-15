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
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.util.MultiLanguageString;

import pt.ist.fenixframework.Atomic;

/**
 * @author Fernanda Quitério
 */
public class EditEvaluation {

    protected Boolean run(final ExecutionCourse executionCourse, final MultiLanguageString evaluationMethod) {
        if (executionCourse.getEvaluationMethod() == null) {
            executionCourse.createEvaluationMethod(evaluationMethod);
        } else {
            executionCourse.getEvaluationMethod().edit(evaluationMethod);
        }
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditEvaluation serviceInstance = new EditEvaluation();

    @Atomic
    public static Boolean runEditEvaluation(ExecutionCourse executionCourse, MultiLanguageString evaluationMethod)
            throws NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourse.getExternalId());
        return serviceInstance.run(executionCourse, evaluationMethod);
    }

}