package net.sourceforge.fenixedu.dataTransferObject.teacher;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;

public class ImportLessonPlanningsBean implements Serializable {

    private DomainReference<ExecutionPeriod> executionPeriodReference;
    
    private DomainReference<ExecutionDegree> executionDegreeReference;
    
    private DomainReference<CurricularYear> curricularYearReference;
    
    private DomainReference<ExecutionCourse> executionCourseReference;
                
    private ImportType importType;

        
    public ExecutionCourse getExecutionCourse() {
        return (this.executionCourseReference != null) ? this.executionCourseReference.getObject() : null;
    }
    
    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = (executionCourse != null) ? new DomainReference<ExecutionCourse>(executionCourse) : null;
    }
    
    public ExecutionPeriod getExecutionPeriod() {
        return (this.executionPeriodReference != null) ? this.executionPeriodReference.getObject() : null;
    }
    
    public void setExecutionPeriod(ExecutionPeriod executionPeriod) {
        this.executionPeriodReference = (executionPeriod != null) ? new DomainReference<ExecutionPeriod>(executionPeriod) : null;
    }
    
    public ExecutionDegree getExecutionDegree() {
        return (this.executionDegreeReference != null) ? this.executionDegreeReference.getObject() : null;
    }
    
    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegreeReference = (executionDegree != null) ? new DomainReference<ExecutionDegree>(executionDegree) : null;
    }
       
    public CurricularYear getCurricularYear() {
        return (this.curricularYearReference != null) ? this.curricularYearReference.getObject() : null;
    }
    
    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYearReference = (curricularYear != null) ? new DomainReference<CurricularYear>(curricularYear) : null;
    }
    
    public ImportType getImportType() {
        return importType;
    }

    public void setImportType(ImportType importType) {
        this.importType = importType;
    }    
    
    public static enum ImportType{
        
        SUMMARIES,
        
        PLANNING;
        
        public String getName() {
            return name();
        }           
    }
}
