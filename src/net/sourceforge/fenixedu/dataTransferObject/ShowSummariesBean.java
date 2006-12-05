package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShowSummariesBean implements Serializable {

    private SummaryTeacherBean summaryTeacher;
    
    private DomainReference<Shift> shiftReference;

    private ShiftType shiftType;
    
    private ListSummaryType listSummaryType;
    
    private DomainReference<ExecutionCourse> executionCourseReference;
    
    private DomainReference<Professorship> professorshipLoggedReference;
    
    protected ShowSummariesBean() {
	// TODO Auto-generated constructor stub
    }
    
    public ShowSummariesBean(SummaryTeacherBean teacher, ExecutionCourse executionCourse, ListSummaryType type, Professorship loggedProfessorship) {
	setSummaryTeacher(teacher);	
	setExecutionCourse(executionCourse);
	setListSummaryType(type);
	setProfessorshipLogged(loggedProfessorship);
    }
    
    public Professorship getProfessorshipLogged() {
	return (this.professorshipLoggedReference != null) ? this.professorshipLoggedReference.getObject() : null;
    }

    public void setProfessorshipLogged(Professorship professorship) {
	this.professorshipLoggedReference = (professorship != null) ? new DomainReference<Professorship>(
		professorship) : null;
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
        ALL_CONTENT, SUMMARIZED;
        public String getName() {
            return name();
        }
    }
}
