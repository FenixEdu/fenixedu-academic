package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.IOException;

import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.file.VirtualPathNode;

public class TeacherEvaluationFile extends TeacherEvaluationFile_Base {

    public TeacherEvaluationFile(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType,
	    String filename, byte[] content, Person createdBy) {
	super();
	setTeacherEvaluation(teacherEvaluation);
	setTeacherEvaluationFileType(teacherEvaluationFileType);
	setCreatedBy(createdBy);
	init(getfilePath(teacherEvaluationFileType), filename, filename, null, content, null);
    }

    private VirtualPath getfilePath(TeacherEvaluationFileType teacherEvaluationFileType) {
	final VirtualPath filePath = new VirtualPath();
	filePath.addNode(new VirtualPathNode("TeacherEvaluationFiles", "TeacherEvaluation Files"));
	filePath.addNode(new VirtualPathNode("TeacherEvaluation" + getIdInternal(), teacherEvaluationFileType.name()));
	return filePath;
    }

    @Service
    public static TeacherEvaluationFile create(FileUploadBean fileUploadBean, Person createdBy) throws IOException {
	return new TeacherEvaluationFile(fileUploadBean.getTeacherEvaluationProcess().getCurrentTeacherEvaluation(),
		fileUploadBean.getTeacherEvaluationFileType(), fileUploadBean.getFilename(), fileUploadBean.getBytes(), createdBy);
    }

}
