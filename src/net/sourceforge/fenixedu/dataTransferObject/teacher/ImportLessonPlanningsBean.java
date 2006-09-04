package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.ImportContentBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;

public class ImportLessonPlanningsBean extends ImportContentBean implements Serializable {

    private ImportType importType;
    
    private DomainReference<Shift> shiftReference;
    
    private DomainReference<ExecutionCourse> executionCourseToReference;

    public ImportLessonPlanningsBean(ExecutionCourse executionCourse) {
        setExecutionCourseTo(executionCourse);
    }
    
    public ExecutionCourse getExecutionCourseTo() {
        return (this.executionCourseToReference != null) ? this.executionCourseToReference.getObject() : null;
    }
    
    public void setExecutionCourseTo(ExecutionCourse executionCourse) {
        this.executionCourseToReference = (executionCourse != null) ? new DomainReference<ExecutionCourse>(executionCourse) : null;
    }
    
    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }    
    
    public Shift getShift() {
        return (this.shiftReference != null) ? this.shiftReference.getObject() : null;
    }

    public void setShift(Shift shift) {
        this.shiftReference = (shift != null) ? new DomainReference<Shift>(shift) : null;
    }
    
    public static enum ImportType{
        
        SUMMARIES,
        
        PLANNING;
        
        public String getName() {
            return name();
        }           
    }
    
}
