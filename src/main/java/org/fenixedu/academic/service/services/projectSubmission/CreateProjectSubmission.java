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
package net.sourceforge.fenixedu.applicationTier.Servico.projectSubmission;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.ProjectSubmissionFile;
import net.sourceforge.fenixedu.domain.ProjectSubmissionLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.ProjectDepartmentGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupGroup;
import net.sourceforge.fenixedu.domain.accessControl.TeacherGroup;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

import com.google.common.io.ByteStreams;

public class CreateProjectSubmission {

    public CreateProjectSubmission() {
        super();
    }

    @Atomic
    public static void run(byte[] bytes, String filename, Attends attends, Project project, StudentGroup studentGroup,
            Person person) throws FenixServiceException, IOException {
        check(RolePredicates.STUDENT_PREDICATE);

        final Group permittedGroup = createPermittedGroup(attends, studentGroup, project);
        createProjectSubmission(bytes, filename, attends, project, studentGroup, permittedGroup);
    }

    private static Group createPermittedGroup(final Attends attends, final StudentGroup studentGroup, final Project project) {
        final ExecutionCourse executionCourse = attends.getExecutionCourse();
        return TeacherGroup.get(executionCourse).or(StudentGroupGroup.get(studentGroup)).or(ProjectDepartmentGroup.get(project));
    }

    private static void checkPermissions(Attends attends, Person person) throws FenixServiceException {
        if (!person.getCurrentAttendsPlusSpecialSeason().contains(attends)) {
            throw new FenixServiceException("error.NotAuthorized");
        }
    }

    private static byte[] read(final InputStream stream) {
        try {
            return ByteStreams.toByteArray(stream);
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    private static ProjectSubmission createProjectSubmission(byte[] bytes, String filename, Attends attends, Project project,
            StudentGroup studentGroup, final Group permittedGroup) throws FenixServiceException {

        final ProjectSubmissionFile projectSubmissionFile = new ProjectSubmissionFile(filename, filename, bytes, permittedGroup);

        final ProjectSubmission projectSubmission = new ProjectSubmission(project, studentGroup, attends, projectSubmissionFile);

        new ProjectSubmissionLog(projectSubmission.getSubmissionDateTime(), filename, projectSubmissionFile.getContentType(),
                projectSubmissionFile.getChecksum(), projectSubmissionFile.getChecksumAlgorithm(), bytes.length, studentGroup,
                attends, project);

        return projectSubmission;

    }

}