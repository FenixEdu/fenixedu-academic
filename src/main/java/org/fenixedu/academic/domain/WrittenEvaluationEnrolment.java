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
package org.fenixedu.academic.domain;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;

public class WrittenEvaluationEnrolment extends WrittenEvaluationEnrolment_Base {

    public WrittenEvaluationEnrolment() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration registration) {
        this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(registration);
    }

    public WrittenEvaluationEnrolment(WrittenEvaluation writtenEvaluation, Registration registration, Space room) {
        this();
        this.setWrittenEvaluation(writtenEvaluation);
        this.setStudent(registration);
        this.setRoom(room);
    }

    public void delete() {
        if (this.getRoom() != null) {
            throw new DomainException("error.notAuthorizedUnEnrollment");
        }

        this.setWrittenEvaluation(null);
        this.setStudent(null);

        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public boolean isForExecutionPeriod(final ExecutionSemester executionSemester) {
        for (final ExecutionCourse executionCourse : getWrittenEvaluation().getAssociatedExecutionCoursesSet()) {
            if (executionCourse.getExecutionPeriod() == executionSemester) {
                return true;
            }
        }
        return false;
    }

}
