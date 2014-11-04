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
package org.fenixedu.academic.service.services.resourceAllocationManager.exams;

import org.fenixedu.academic.domain.Exam;
import org.fenixedu.academic.domain.WrittenEvaluation;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.ExamDateCertificateRequest;
import org.fenixedu.academic.service.filter.EditWrittenEvaluationAuthorization;
import org.fenixedu.academic.service.filter.ExecutionCourseCoordinatorAuthorizationFilter;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.filter.ResourceAllocationManagerAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.bennu.signals.DomainObjectEvent;
import org.fenixedu.bennu.signals.Signal;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteWrittenEvaluation {

    /**
     * @param Integer
     *            executionCourseOID used in filtering
     *            (ExecutionCourseLecturingTeacherAuthorizationFilter)
     */
    protected void run(String executionCourseOID, String writtenEvaluationOID) throws FenixServiceException {
        final WrittenEvaluation writtenEvaluationToDelete =
                (WrittenEvaluation) FenixFramework.getDomainObject(writtenEvaluationOID);
        if (writtenEvaluationToDelete == null) {
            throw new FenixServiceException("error.noWrittenEvaluation");
        }
        if (writtenEvaluationToDelete instanceof Exam) {
            disconnectExamCertificateRequests(writtenEvaluationToDelete);
        }
        Signal.emit("academic.writtenevaluation.deleted", new DomainObjectEvent<>(writtenEvaluationToDelete));
        writtenEvaluationToDelete.delete();
    }

    private void disconnectExamCertificateRequests(WrittenEvaluation writtenEvaluationToDelete) {
        Exam examToDelete = (Exam) writtenEvaluationToDelete;
        for (ExamDateCertificateRequest examDateCertificateRequest : examToDelete.getExamDateCertificateRequestsSet()) {
            examDateCertificateRequest.removeExams(examToDelete);
        }
    }

    // Service Invokers migrated from Berserk

    private static final DeleteWrittenEvaluation serviceInstance = new DeleteWrittenEvaluation();

    @Atomic
    public static void runDeleteWrittenEvaluation(String executionCourseOID, String writtenEvaluationOID)
            throws FenixServiceException, NotAuthorizedException {
        EditWrittenEvaluationAuthorization.instance.execute(writtenEvaluationOID);
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(executionCourseOID, writtenEvaluationOID);
        } catch (NotAuthorizedException ex1) {
            try {
                ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseOID);
                serviceInstance.run(executionCourseOID, writtenEvaluationOID);
            } catch (NotAuthorizedException ex2) {
                try {
                    ExecutionCourseCoordinatorAuthorizationFilter.instance.execute(executionCourseOID);
                    serviceInstance.run(executionCourseOID, writtenEvaluationOID);
                } catch (NotAuthorizedException ex3) {
                    throw ex3;
                }
            }
        }
    }

}