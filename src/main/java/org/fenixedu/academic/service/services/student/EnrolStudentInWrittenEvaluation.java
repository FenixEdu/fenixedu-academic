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
package org.fenixedu.academic.service.services.student;

import java.util.Set;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.service.ServiceMonitoring;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EnrolStudentInWrittenEvaluation {

    protected void run(String username, String writtenEvaluationOID) throws FenixServiceException {

        ServiceMonitoring.logService(this.getClass(), username, writtenEvaluationOID);

        final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationOID);
        final Person person = Person.readPersonByUsername(username);
        final Student student = person.getStudent();
        final Registration registration = findCorrectRegistration(student, writtenEvaluation.getAssociatedExecutionCoursesSet());
        if (writtenEvaluation == null || registration == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!writtenEvaluation.isInEnrolmentPeriod()) {
            throw new FenixServiceException("Enrolment period is not opened");
        }

        enrolmentAction(writtenEvaluation, registration);
    }

    private Registration findCorrectRegistration(final Student student, final Set<ExecutionCourse> associatedExecutionCoursesSet) {
        return student.getRegistrationsSet().stream()
                //.filter(registration -> registration.isActive())
                .flatMap(registration -> registration.getAssociatedAttendsSet().stream())
                .filter(attends -> associatedExecutionCoursesSet.contains(attends.getExecutionCourse()))
                .map(attends -> attends.getRegistration())
                .findAny().orElse(null);
    }

    public void enrolmentAction(final WrittenEvaluation writtenEvaluation, final Registration registration) {
        writtenEvaluation.enrolStudent(registration);
    }

    // Service Invokers migrated from Berserk

    private static final EnrolStudentInWrittenEvaluation serviceInstance = new EnrolStudentInWrittenEvaluation();

    @Atomic
    public static void runEnrolStudentInWrittenEvaluation(String username, String writtenEvaluationOID)
            throws FenixServiceException {
        serviceInstance.run(username, writtenEvaluationOID);
    }

}