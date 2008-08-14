package net.sourceforge.fenixedu.applicationTier.Servico.projectSubmission;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
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
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.presentationTier.Action.manager.FileContentCreationBean.EducationalResourceType;
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

    public void run(java.io.File uploadFile, String filename, Attends attends, Project project, StudentGroup studentGroup,
	    Person person) throws FenixServiceException, IOException {

	checkPermissions(attends, person);
	final Group permittedGroup = createPermittedGroup(attends, studentGroup);
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

    private Group createPermittedGroup(Attends attends, StudentGroup studentGroup) {
	final ExecutionCourse executionCourse = attends.getExecutionCourse();
	return new GroupUnion(new ExecutionCourseTeachersGroup(executionCourse), new StudentGroupStudentsGroup(studentGroup));
    }

    private void checkPermissions(Attends attends, Person person) throws FenixServiceException {
	if (!person.getCurrentAttends().contains(attends)) {
	    throw new FenixServiceException("error.NotAuthorized");
	}
    }

    private ProjectSubmission createProjectSubmission(InputStream inputStream, String filename, Attends attends, Project project,
	    StudentGroup studentGroup, final Group permittedGroup) throws FenixServiceException, FileManagerException {

	final String fileToDeleteExternalId = getFileToDeleteExternalId(project, studentGroup);
	final VirtualPath filePath = getVirtualPath(attends.getExecutionCourse(), project, studentGroup);

	Collection<FileSetMetaData> metaData = new ArrayList<FileSetMetaData>();
	metaData.add(FileSetMetaData.createAuthorMeta(attends.getRegistration().getPerson().getName()));
	metaData.add(FileSetMetaData.createTitleMeta(filename));
	metaData.add(new FileSetMetaData("type", null, null, EducationalResourceType.PROJECT_SUBMISSION.toString()));

	final FileDescriptor fileDescriptor = FileManagerFactory.getFactoryInstance().getFileManager().saveFile(filePath,
		filename, (permittedGroup != null) ? true : false, metaData, inputStream);

	final ProjectSubmissionFile projectSubmissionFile = new ProjectSubmissionFile(filename, filename, fileDescriptor
		.getMimeType(), fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(),
		fileDescriptor.getUniqueId(), permittedGroup);
	final ProjectSubmission projectSubmission = new ProjectSubmission(project, studentGroup, attends, projectSubmissionFile);

	new ProjectSubmissionLog(projectSubmission.getSubmissionDateTime(), filename, fileDescriptor.getMimeType(),
		fileDescriptor.getChecksum(), fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), studentGroup,
		attends, project);

	if (fileToDeleteExternalId != null) {
	    new DeleteFileRequest(AccessControl.getPerson(), fileToDeleteExternalId);
	}

	return projectSubmission;

    }

    private String getFileToDeleteExternalId(Project project, StudentGroup studentGroup) {
	String fileToDeleteStorageId = null;
	if (!project.canAddNewSubmissionWithoutExceedLimit(studentGroup)) {
	    fileToDeleteStorageId = project.getOldestProjectSubmissionForStudentGroup(studentGroup).getProjectSubmissionFile()
		    .getExternalStorageIdentification();
	}

	return fileToDeleteStorageId;
    }

    private VirtualPath getVirtualPath(final ExecutionCourse executionCourse, final Project project,
	    final StudentGroup studentGroup) {
	final VirtualPath filePath = new VirtualPath();

	filePath.addNode(new VirtualPathNode("GRP" + studentGroup.getIdInternal(), studentGroup.getGroupNumber().toString()));

	filePath.addNode(0, new VirtualPathNode("PRJ" + project.getIdInternal(), project.getName()));
	filePath.addNode(0, new VirtualPathNode("EC" + executionCourse.getIdInternal(), executionCourse.getNome()));

	final ExecutionSemester executionSemester = executionCourse.getExecutionPeriod();
	filePath.addNode(0, new VirtualPathNode("EP" + executionSemester.getIdInternal(), executionSemester.getName()));

	final ExecutionYear executionYear = executionSemester.getExecutionYear();
	filePath.addNode(0, new VirtualPathNode("EY" + executionYear.getIdInternal(), executionYear.getYear()));

	filePath.addNode(0, new VirtualPathNode("Courses", "Courses"));
	return filePath;
    }
}