package net.sourceforge.fenixedu.domain.teacher.evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.PersonGroup;
import net.sourceforge.fenixedu.domain.accessControl.WebSiteManagersGroup;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.Atomic;

public class TeacherEvaluationFile extends TeacherEvaluationFile_Base {

    public TeacherEvaluationFile(TeacherEvaluation teacherEvaluation, TeacherEvaluationFileType teacherEvaluationFileType,
            String filename, byte[] content, Person createdBy) {
        super();
        setTeacherEvaluation(teacherEvaluation);
        setTeacherEvaluationFileType(teacherEvaluationFileType);
        setCreatedBy(createdBy);
        filename = processFilename(teacherEvaluation, teacherEvaluationFileType, getExtension(filename));

        final Collection<IGroup> groups = new ArrayList<>();

        final TeacherEvaluationProcess process = teacherEvaluation.getTeacherEvaluationProcess();
        groups.add(new PersonGroup(process.getEvaluator()));
        groups.add(new PersonGroup(process.getEvaluee()));
        final UnitSite unitSite = Bennu.getInstance().getTeacherEvaluationCoordinatorCouncil();
        if (unitSite != null) {
            groups.add(new WebSiteManagersGroup(unitSite));
        }

        init(filename, filename, content, new GroupUnion(groups));
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

    @Override
    public void delete() {
        setTeacherEvaluation(null);
        setCreatedBy(null);
        super.delete();
    }

    @Atomic
    public static TeacherEvaluationFile create(FileUploadBean fileUploadBean, Person createdBy) throws IOException {
        return new TeacherEvaluationFile(fileUploadBean.getTeacherEvaluationProcess().getCurrentTeacherEvaluation(),
                fileUploadBean.getTeacherEvaluationFileType(), fileUploadBean.getFilename(), fileUploadBean.getBytes(), createdBy);
    }

    public TeacherEvaluationFile copy(TeacherEvaluation evaluation) {
        return new TeacherEvaluationFile(evaluation, getTeacherEvaluationFileType(), getFilename(), getContents(), getCreatedBy());
    }

    @Deprecated
    public boolean hasTeacherEvaluation() {
        return getTeacherEvaluation() != null;
    }

    @Deprecated
    public boolean hasTeacherEvaluationFileType() {
        return getTeacherEvaluationFileType() != null;
    }

    @Deprecated
    public boolean hasCreatedBy() {
        return getCreatedBy() != null;
    }

}
