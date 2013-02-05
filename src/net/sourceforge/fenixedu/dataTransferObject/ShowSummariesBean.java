package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SummaryTeacherBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShowSummariesBean implements Serializable {

    private SummaryTeacherBean summaryTeacher;

    private Shift shiftReference;

    private ShiftType shiftType;

    private ListSummaryType listSummaryType;

    private ExecutionCourse executionCourseReference;

    private Professorship professorshipLoggedReference;

    private SummariesOrder summariesOrder;

    protected ShowSummariesBean() {
        // TODO Auto-generated constructor stub
    }

    public ShowSummariesBean(SummaryTeacherBean teacher, ExecutionCourse executionCourse, ListSummaryType type,
            Professorship loggedProfessorship) {
        setSummaryTeacher(teacher);
        setExecutionCourse(executionCourse);
        setListSummaryType(type);
        setProfessorshipLogged(loggedProfessorship);
        setSummariesOrder(SummariesOrder.DECREASING);
    }

    public Professorship getProfessorshipLogged() {
        return this.professorshipLoggedReference;
    }

    public void setProfessorshipLogged(Professorship professorship) {
        this.professorshipLoggedReference = professorship;
    }

    public SummaryTeacherBean getSummaryTeacher() {
        return summaryTeacher;
    }

    public void setSummaryTeacher(SummaryTeacherBean summaryTeacher) {
        this.summaryTeacher = summaryTeacher;
    }

    public Shift getShift() {
        return this.shiftReference;
    }

    public void setShift(Shift shift) {
        this.shiftReference = shift;
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
        return this.executionCourseReference;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourseReference = executionCourse;
    }

    public SummariesOrder getSummariesOrder() {
        return summariesOrder;
    }

    public void setSummariesOrder(SummariesOrder summariesOrder) {
        this.summariesOrder = summariesOrder;
    }

    public static enum ListSummaryType {

        ALL_CONTENT, SUMMARIZED;

        public String getName() {
            return name();
        }
    }

    public static enum SummariesOrder {

        GROWING, DECREASING;

        public String getName() {
            return name();
        }
    }
}
