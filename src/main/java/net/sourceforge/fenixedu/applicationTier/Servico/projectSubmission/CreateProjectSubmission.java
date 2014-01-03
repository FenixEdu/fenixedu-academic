package net.sourceforge.fenixedu.applicationTier.Servico.projectSubmission;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.FileContent.EducationalResourceType;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.ProjectSubmissionFile;
import net.sourceforge.fenixedu.domain.ProjectSubmissionLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ProjectDepartmentAccessGroup;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.io.IOUtils;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateProjectSubmission {

    public CreateProjectSubmission() {
        super();
    }

    @Atomic
    public static void run(java.io.File uploadFile, String filename, Attends attends, Project project, StudentGroup studentGroup,
            Person person) throws FenixServiceException, IOException {
        check(RolePredicates.STUDENT_PREDICATE);

//	checkPermissions(attends, person);
        final Group permittedGroup = createPermittedGroup(attends, studentGroup, project);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(uploadFile);
            createProjectSubmission(inputStream, filename, attends, project, studentGroup, permittedGroup);
        } catch (FileNotFoundException e) {
            new FenixServiceException(e.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static Group createPermittedGroup(final Attends attends, final StudentGroup studentGroup, final Project project) {
        final ExecutionCourse executionCourse = attends.getExecutionCourse();
        return new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse), new StudentGroupStudentsGroup(studentGroup),
                new ProjectDepartmentAccessGroup(project));
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

    private static ProjectSubmission createProjectSubmission(InputStream inputStream, String filename, Attends attends,
            Project project, StudentGroup studentGroup, final Group permittedGroup) throws FenixServiceException,
            FileManagerException {

        final String fileToDeleteExternalId = getFileToDeleteExternalId(project, studentGroup);
        final VirtualPath filePath = getVirtualPath(attends.getExecutionCourse(), project, studentGroup);

        Collection<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
        metaData.add(FileSetMetaData.createAuthorMeta(attends.getRegistration().getPerson().getName()));
        metaData.add(FileSetMetaData.createTitleMeta(filename));
        metaData.add(new FileSetMetaData("type", null, null, EducationalResourceType.PROJECT_SUBMISSION.toString()));

        final byte[] bs = read(inputStream);
        final ProjectSubmissionFile projectSubmissionFile =
                new ProjectSubmissionFile(filePath, filename, filename, metaData, bs, permittedGroup);

        final ProjectSubmission projectSubmission = new ProjectSubmission(project, studentGroup, attends, projectSubmissionFile);

        new ProjectSubmissionLog(projectSubmission.getSubmissionDateTime(), filename, projectSubmissionFile.getMimeType(),
                projectSubmissionFile.getChecksum(), projectSubmissionFile.getChecksumAlgorithm(), bs.length, studentGroup,
                attends, project);

        if (fileToDeleteExternalId != null) {
            new DeleteFileRequest(AccessControl.getPerson(), fileToDeleteExternalId);
        }

        return projectSubmission;

    }

    private static String getFileToDeleteExternalId(Project project, StudentGroup studentGroup) {
        String fileToDeleteStorageId = null;
        if (!project.canAddNewSubmissionWithoutExceedLimit(studentGroup)) {
            fileToDeleteStorageId =
                    project.getOldestProjectSubmissionForStudentGroup(studentGroup).getProjectSubmissionFile()
                            .getExternalStorageIdentification();
        }

        return fileToDeleteStorageId;
    }

    private static VirtualPath getVirtualPath(final ExecutionCourse executionCourse, final Project project,
            final StudentGroup studentGroup) {
        final VirtualPath filePath = new VirtualPath();

        filePath.addNode(new VirtualPathNode("GRP" + studentGroup.getExternalId(), studentGroup.getGroupNumber().toString()));

        filePath.addNode(0, new VirtualPathNode("PRJ" + project.getExternalId(), project.getName()));
        filePath.addNode(0, new VirtualPathNode("EC" + executionCourse.getExternalId(), executionCourse.getNome()));

        final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
        filePath.addNode(0, new VirtualPathNode("EP" + executionSemester.getExternalId(), executionSemester.getName()));

        final ExecutionYear executionYear = executionSemester.getExecutionYear();
        filePath.addNode(0, new VirtualPathNode("EY" + executionYear.getExternalId(), executionYear.getYear()));

        filePath.addNode(0, new VirtualPathNode("Courses", "Courses"));
        return filePath;
    }
}