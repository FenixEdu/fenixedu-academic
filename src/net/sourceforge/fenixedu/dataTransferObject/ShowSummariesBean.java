package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShowSummariesBean implements Serializable {

    private SummaryTeacherBean summaryTeacher;
    
    private DomainReference<Shift> shiftReference;

    private ShiftType shiftType;
    
    private ListSummaryType listSummaryType;
    
    private DomainReference<ExecutionCourse> executionCourseReference;
    
    
    public ShowSummariesBean(SummaryTeacherBean teacher, ExecutionCourse executionCourse, ListSummaryType type) {
	setSummaryTeacher(teacher);	
	setExecutionCourse(executionCourse);
	setListSummaryType(type);
    }
    
    public SummaryTeacherBean getSummaryTeacher() {
        return summaryTeacher;
    }

    public void setSummaryTeacher(SummaryTeacherBean summaryTeacher) {
        this.summaryTeacher = summaryTeacher;
    }
       
    public Shift getShift() {
        return (this.shiftReference != null) ? this.shiftReference.getObject() : null;
    }

    public void setShift(Shift shift) {
        this.shiftReference = (shift != null) ? new DomainReference<Shift>(shift) : null;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }    
    
    public ListSummaryType getListSummaryType() {
        return listSummaryType;
    }

    public void setListSummaryType(ListSummaryType listSummaryType) {
        this.listSummaryType = listSummaryType;
    }
    
    public ExecutionCourse getExecutionCourse() {
        return (this.executionCourseReference != null) ? this.executionCourseReference.getObject() : null;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = (executionCourse != null) ? new DomainReference<ExecutionCourse>(
                executionCourse) : null;
    }
    
    public static enum ListSummaryType {
        ALL, SUMMARIZED;
        public String getName() {
            return name();
        }
    }
}
