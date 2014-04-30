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

import org.apache.commons.io.IOUtils;
import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;

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
            return IOUtils.toByteArray(stream);
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