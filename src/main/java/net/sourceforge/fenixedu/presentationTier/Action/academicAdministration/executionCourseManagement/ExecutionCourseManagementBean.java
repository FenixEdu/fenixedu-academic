package net.sourceforge.fenixedu.presentationTier.Action.academicAdministration.executionCourseManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularYear;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionDegree;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class ExecutionCourseManagementBean implements java.io.Serializable, HasExecutionDegree {

    private static final long serialVersionUID = 1L;

    private ExecutionSemester semester;
    private String acronym;
    private String name;
    private EntryPhase entryPhase;
    private String comments;

    private List<CurricularCourse> curricularCourseList;

    private DegreeCurricularPlan degreeCurricularPlan;

    private ExecutionCourse executionCourse;

    private CurricularYear curricularYear;

    public ExecutionCourseManagementBean(ExecutionSemester semester) {
        setSemester(semester);
        setCurricularCourseList(new ArrayList<CurricularCourse>());
    }

    public ExecutionCourseManagementBean(final ExecutionSemester semester, final CurricularCourse curricularCourse) {
        setSemester(semester);
        setCurricularCourseList(new ArrayList<CurricularCourse>());
        getCurricularCourseList().add(curricularCourse);
        setName(curricularCourse.getNameI18N().getContent(Language.pt));
    }

    public ExecutionCourseManagementBean(final ExecutionCourse executionCourse) {
        setExecutionCourse(executionCourse);
    }

    public ExecutionCourseManagementBean(final DegreeCurricularPlan degreeCurricularPlan) {
        setDegreeCurricularPlan(degreeCurricularPlan);
    }

    public ExecutionCourseManagementBean() {

    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExecutionCourse getExecutionCourse() {
        return executionCourse;
    }

    public void setExecutionCourse(ExecutionCourse executionCourse) {
        this.executionCourse = executionCourse;
    }

    public ExecutionSemester getSemester() {
        return semester;
    }

    public void setSemester(ExecutionSemester semester) {
        this.semester = semester;
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    public void setDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public List<CurricularCourse> getCurricularCourseList() {
        return curricularCourseList;
    }

    public void setCurricularCourseList(List<CurricularCourse> curricularCourseList) {
        this.curricularCourseList = curricularCourseList;
    }

    public CurricularYear getCurricularYear() {
        return curricularYear;
    }

    public void setCurricularYear(CurricularYear curricularYear) {
        this.curricularYear = curricularYear;
    }

    @Override
    public ExecutionDegree getExecutionDegree() {
        if (semester != null && degreeCurricularPlan != null) {
            ExecutionDegree degree = degreeCurricularPlan.getExecutionDegreeByAcademicInterval(semester.getAcademicInterval());

            return degree;
        }

        return null;
    }

}
