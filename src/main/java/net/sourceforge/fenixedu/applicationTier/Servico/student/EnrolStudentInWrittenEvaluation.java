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
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.ServiceMonitoring;
import net.sourceforge.fenixedu.applicationTier.Filtro.ExamStudentAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
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

        enrolmentAction(writtenEvaluation, registration);
    }

    private Registration findCorrectRegistration(final Student student, final Set<ExecutionCourse> associatedExecutionCoursesSet) {
        for (final Registration registration : student.getRegistrationsSet()) {
            if (registration.isActive()) {
                for (final Attends attends : registration.getAssociatedAttendsSet()) {
                    final ExecutionCourse executionCourse = attends.getExecutionCourse();
                    if (associatedExecutionCoursesSet.contains(executionCourse)) {
                        return registration;
                    }
                }
            }
        }
        return null;
    }

    public void enrolmentAction(final WrittenEvaluation writtenEvaluation, final Registration registration) {
        writtenEvaluation.enrolStudent(registration);
    }

    // Service Invokers migrated from Berserk

    private static final EnrolStudentInWrittenEvaluation serviceInstance = new EnrolStudentInWrittenEvaluation();

    @Atomic
    public static void runEnrolStudentInWrittenEvaluation(String username, String writtenEvaluationOID)
            throws FenixServiceException, NotAuthorizedException {
        ExamStudentAuthorizationFilter.instance.execute(username, writtenEvaluationOID);
        serviceInstance.run(username, writtenEvaluationOID);
    }

}