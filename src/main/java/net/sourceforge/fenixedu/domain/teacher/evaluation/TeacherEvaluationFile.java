package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.StringUtils;

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
        filename = processFilename(teacherEvaluation, teacherEvaluationFileType, getExtension(filename));
        init(getfilePath(teacherEvaluationFileType), filename, filename, null, content, null);
    }

    private String getExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        return dot < 0 ? "txt" : filename.substring(dot + 1, filename.length());
    }

    private String processFilename(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType,
            String extension) {
        List<String> parts = new Vector<String>(4);
        if (!teacherEvaluationFileType.isAutoEvaluationFile()) {
            parts.add("res");
        }
        parts.add(teacherEvaluation.getFilenameTypePrefix());
        try {
            parts.add("d"
                    + teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getTeacher().getPerson().getEmployee()
                            .getEmployeeNumber());
        } catch (NullPointerException e) {
            parts.add(teacherEvaluation.getTeacherEvaluationProcess().getEvaluee().getIstUsername());
        }
        parts.add(teacherEvaluation.getTeacherEvaluationProcess().getFacultyEvaluationProcess().getSuffix());
        return StringUtils.join(parts, "_") + "." + extension;
    }

    private VirtualPath getfilePath(TeacherEvaluationFileType teacherEvaluationFileType) {
        final VirtualPath filePath = new VirtualPath();
        filePath.addNode(new VirtualPathNode("TeacherEvaluationFiles", "TeacherEvaluation Files"));
        filePath.addNode(new VirtualPathNode("TeacherEvaluation" + getExternalId(), teacherEvaluationFileType.name()));
        return filePath;
    }

    @Override
    public void delete() {
        setTeacherEvaluation(null);
        setCreatedBy(null);
        super.delete();
    }

    @Service
    public static TeacherEvaluationFile create(FileUploadBean fileUploadBean, Person createdBy) throws IOException {
        return new TeacherEvaluationFile(fileUploadBean.getTeacherEvaluationProcess().getCurrentTeacherEvaluation(),
                fileUploadBean.getTeacherEvaluationFileType(), fileUploadBean.getFilename(), fileUploadBean.getBytes(), createdBy);
    }

    public TeacherEvaluationFile copy(TeacherEvaluation evaluation) {
        return new TeacherEvaluationFile(evaluation, getTeacherEvaluationFileType(), getFilename(), getContents(), getCreatedBy());
    }

}
