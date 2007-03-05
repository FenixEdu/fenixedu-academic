package net.sourceforge.fenixedu.applicationTier.Servico.projectSubmission;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.projectSubmission.CreateProjectSubmissionBean;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Project;
import net.sourceforge.fenixedu.domain.ProjectSubmission;
import net.sourceforge.fenixedu.domain.ProjectSubmissionFile;
import net.sourceforge.fenixedu.domain.ProjectSubmissionLog;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.accessControl.ExecutionCourseTeachersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.StudentGroupStudentsGroup;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileItemCreationBean.EducationalResourceType;
import pt.utl.ist.fenix.tools.file.FileDescriptor;
import pt.utl.ist.fenix.tools.file.FileManagerException;
import pt.utl.ist.fenix.tools.file.FileManagerFactory;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class CreateProjectSubmission extends Service {

    public CreateProjectSubmission() {
        super();
    }

    public void run(CreateProjectSubmissionBean createProjectSubmissionBean)
            throws FenixServiceException, FileManagerException {

        checkPermissions(createProjectSubmissionBean);
        final Group permittedGroup = createPermittedGroup(createProjectSubmissionBean);
        createProjectSubmission(createProjectSubmissionBean, permittedGroup);

    }

    private void checkPermissions(CreateProjectSubmissionBean createProjectSubmissionBean)
            throws FenixServiceException {
        final Attends attends = createProjectSubmissionBean.getAttends();
        final Person person = createProjectSubmissionBean.getPerson();

        if (!person.getCurrentAttends().contains(attends)) {
            throw new FenixServiceException("error.NotAuthorized");
        }
    }

    private Group createPermittedGroup(CreateProjectSubmissionBean createProjectSubmissionBean) {
        final ExecutionCourse executionCourse = createProjectSubmissionBean.getAttends()
                .getExecutionCourse();
        final StudentGroup studentGroup = createProjectSubmissionBean.getStudentGroup();
        return new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse),
                new StudentGroupStudentsGroup(studentGroup));
    }

    private ProjectSubmission createProjectSubmission(
            final CreateProjectSubmissionBean createProjectSubmissionBean, final Group permittedGroup)
            throws FenixServiceException, FileManagerException {

        final InputStream inputStream = createProjectSubmissionBean.getInputStream();
        final String filename = createProjectSubmissionBean.getFilename();
        final Attends attends = createProjectSubmissionBean.getAttends();
        final Project project = createProjectSubmissionBean.getProject();
        final StudentGroup studentGroup = createProjectSubmissionBean.getStudentGroup();
        final String fileToDeleteExternalId = getFileToDeleteExternalId(project, studentGroup);
        final VirtualPath filePath = getVirtualPath(attends.getExecutionCourse(), project, studentGroup);
      
        Collection<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
        metaData.add(FileSetMetaData.createAuthorMeta(attends.getAluno().getPerson().getNome()));
        metaData.add(FileSetMetaData.createTitleMeta(filename));
        metaData.add(new FileSetMetaData("type",null,null,EducationalResourceType.PROJECT_SUBMISSION.toString()));
        
        final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager().saveFile(filePath,
                filename, (permittedGroup != null) ? true : false, metaData, inputStream);

        final ProjectSubmissionFile projectSubmissionFile = new ProjectSubmissionFile(filename,
                filename, fileDescriptor.getMimeType(), fileDescriptor.getChecksum(), fileDescriptor
                        .getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor.getUniqueId(),
                permittedGroup);
        final ProjectSubmission projectSubmission = new ProjectSubmission(project, studentGroup,
                attends, projectSubmissionFile);

        new ProjectSubmissionLog(projectSubmission.getSubmissionDateTime(), filename, fileDescriptor
                .getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(),
                fileDescriptor.getSize(), studentGroup, attends, project);

        if (fileToDeleteExternalId != null) {
            FileManagerFactory.getFactoryInstance().getFileManager().deleteFile(fileToDeleteExternalId);
        }

        return projectSubmission;

    }

    private String getFileToDeleteExternalId(Project project, StudentGroup studentGroup) {
        String fileToDeleteStorageId = null;
        if (!project.canAddNewSubmissionWithoutExceedLimit(studentGroup)) {
            fileToDeleteStorageId = project.getOldestProjectSubmissionForStudentGroup(studentGroup)
                    .getProjectSubmissionFile().getExternalStorageIdentification();
        }

        return fileToDeleteStorageId;
    }

    private VirtualPath getVirtualPath(final ExecutionCourse executionCourse, final Project project,
            final StudentGroup studentGroup) {
        final VirtualPath filePath = new VirtualPath();

        filePath.addNode(new VirtualPathNode("GRP" + studentGroup.getIdInternal(), studentGroup.getGroupNumber()
                .toString()));

        filePath.addNode(0, new VirtualPathNode("PRJ" + project.getIdInternal(), project.getName()));
        filePath.addNode(0, new VirtualPathNode("EC" + executionCourse.getIdInternal(), executionCourse.getNome()));

        final ExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
        filePath.addNode(0, new VirtualPathNode("EP" + executionPeriod.getIdInternal(), executionPeriod.getName()));

        final ExecutionYear executionYear = executionPeriod.getExecutionYear();
        filePath.addNode(0, new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));

        filePath.addNode(0, new VirtualPathNode("Courses","Courses"));
        return filePath;
    }
}