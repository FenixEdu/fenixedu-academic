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
package org.fenixedu.academic.service.services.projectSubmission;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.io.IOException;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Project;
import org.fenixedu.academic.domain.ProjectSubmission;
import org.fenixedu.academic.domain.ProjectSubmissionFile;
import org.fenixedu.academic.domain.ProjectSubmissionLog;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class CreateProjectSubmission {

    @Atomic
    public static void run(byte[] bytes, String filename, Attends attends, Project project, StudentGroup studentGroup,
            Person person) throws FenixServiceException, IOException {
        check(RolePredicates.STUDENT_PREDICATE);
        final ProjectSubmissionFile projectSubmissionFile = new ProjectSubmissionFile(filename, filename, bytes);

        final ProjectSubmission projectSubmission = new ProjectSubmission(project, studentGroup, attends, projectSubmissionFile);

        new ProjectSubmissionLog(projectSubmission.getSubmissionDateTime(), filename, projectSubmissionFile.getContentType(),
                projectSubmissionFile.getChecksum(), projectSubmissionFile.getChecksumAlgorithm(), bytes.length, studentGroup,
                attends, project);
    }

}