/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoCurriculumHistoricReport extends DataTranferObject {

    Integer enrolled;

    Integer evaluated;

    Integer aproved;

    Integer semester;

    InfoCurricularCourse infoCurricularCourse;

    InfoExecutionYear infoExecutionYear;

    List enrollments;

    /**
     * @return Returns the aproved.
     */
    public Integer getAproved() {
        return aproved;
    }

    /**
     * @param aproved
     *            The aproved to set.
     */
    public void setAproved(Integer aproved) {
        this.aproved = aproved;
    }

    /**
     * @return Returns the enrolled.
     */
    public Integer getEnrolled() {
        return enrolled;
    }

    /**
     * @param enrolled
     *            The enrolled to set.
     */
    public void setEnrolled(Integer enrolled) {
        this.enrolled = enrolled;
    }

    /**
     * @return Returns the evaluated.
     */
    public Integer getEvaluated() {
        return evaluated;
    }

    /**
     * @param evaluated
     *            The evaluated to set.
     */
    public void setEvaluated(Integer evaluated) {
        this.evaluated = evaluated;
    }

    /**
     * @return Returns the enrollments.
     */
    public List getEnrollments() {
        return enrollments;
    }

    /**
     * @param enrollments
     *            The enrollments to set.
     */
    public void setEnrollments(List enrollments) {
        this.enrollments = enrollments;
    }

    /**
     * @return Returns the infoCurricularCourse.
     */
    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @param infoCurricularCourse
     *            The infoCurricularCourse to set.
     */
    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
     * @return Returns the semester.
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * @param semester
     *            The semester to set.
     */
    public void setSemester(Integer semester) {
        this.semester = semester;
    }
}