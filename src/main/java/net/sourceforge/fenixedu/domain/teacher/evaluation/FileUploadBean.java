package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.FileUtils;

public class FileUploadBean implements Serializable {

    private transient InputStream inputStream;
    private String filename;
    private String displayName;
    private byte[] bytes;
    private FacultyEvaluationProcess facultyEvaluationProcess;
    private TeacherEvaluationProcess teacherEvaluationProcess;
    private TeacherEvaluationFileType teacherEvaluationFileType;

    public FileUploadBean() {
    }

    public FileUploadBean(final FacultyEvaluationProcess facultyEvaluationProcess) {
        this();
        this.facultyEvaluationProcess = facultyEvaluationProcess;
    }

    public FileUploadBean(final TeacherEvaluationProcess teacherEvaluationProcess,
            TeacherEvaluationFileType teacherEvaluationFileType) {
        this();
        this.teacherEvaluationProcess = teacherEvaluationProcess;
        this.teacherEvaluationFileType = teacherEvaluationFileType;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public FacultyEvaluationProcess getFacultyEvaluationProcess() {
        return facultyEvaluationProcess;
    }

    public void setFacultyEvaluationProcess(FacultyEvaluationProcess facultyEvaluationProcess) {
        this.facultyEvaluationProcess = facultyEvaluationProcess;
    }

    public void consumeInputStream() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        FileUtils.copy(inputStream, byteArrayOutputStream);
        bytes = byteArrayOutputStream.toByteArray();
    }

    @Atomic
    public void uploadEvaluators() {
        facultyEvaluationProcess.uploadEvaluators(bytes);
    }

    @Atomic
    public void uploadApprovedEvaluations() {
        facultyEvaluationProcess.uploadApprovedEvaluations(bytes);
    }

    public TeacherEvaluationProcess getTeacherEvaluationProcess() {
        return teacherEvaluationProcess;
    }

    public void setTeacherEvaluationProcess(TeacherEvaluationProcess teacherEvaluationProcess) {
        this.teacherEvaluationProcess = teacherEvaluationProcess;
    }

    public TeacherEvaluationFileType getTeacherEvaluationFileType() {
        return teacherEvaluationFileType;
    }

    public void setTeacherEvaluationFileType(TeacherEvaluationFileType teacherEvaluationFileType) {
        this.teacherEvaluationFileType = teacherEvaluationFileType;
    }

}
