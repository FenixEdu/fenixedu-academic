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
        super();
	setExecutionCourseTo(executionCourse);
	setShift(null);
    }
    
    public ExecutionCourse getExecutionCourseTo() {
        return this.executionCourseToReference.getObject();
    }
    
    public void setExecutionCourseTo(ExecutionCourse executionCourse) {
        this.executionCourseToReference = new DomainReference<ExecutionCourse>(executionCourse);
    }
    
    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }    
    
    public Shift getShift() {
        return this.shiftReference.getObject();
    }

    public void setShift(Shift shift) {
        this.shiftReference = new DomainReference<Shift>(shift);
    }
    
    public static enum ImportType{
        
        SUMMARIES,
        
        PLANNING;
        
        public String getName() {
            return name();
        }           
    }
    
}
