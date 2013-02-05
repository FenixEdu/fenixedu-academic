package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class ImportLessonPlanningsBean extends ImportContentBean implements Serializable {

    private ImportType importType;

    private Shift shiftReference;

    private ExecutionCourse executionCourseToReference;

    public ImportLessonPlanningsBean(ExecutionCourse executionCourse) {
        super();
        setExecutionCourseTo(executionCourse);
        setShift(null);
    }

    public ExecutionCourse getExecutionCourseTo() {
        return this.executionCourseToReference;
    }

    public void setExecutionCourseTo(ExecutionCourse executionCourse) {
        this.executionCourseToReference = executionCourse;
    }

    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }

    public Shift getShift() {
        return this.shiftReference;
    }

    public void setShift(Shift shift) {
        this.shiftReference = shift;
    }

    public static enum ImportType {

        SUMMARIES,

        PLANNING;

        public String getName() {
            return name();
        }
    }

}
