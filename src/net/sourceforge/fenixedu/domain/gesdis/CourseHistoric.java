/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.domain.gesdis;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.ICurricularCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class CourseHistoric extends DomainObject implements ICourseHistoric {

    private Integer keyCurricularCourse;

    private Integer enrolled;

    private Integer evaluated;

    private Integer approved;

    private String curricularYear;

    private Integer semester;

    private ICurricularCourse curricularCourse;

    public CourseHistoric() {
    }

    /** Creates a new instance of CourseReport */
    public CourseHistoric(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return Returns the approved.
     */
    public Integer getApproved() {
        return approved;
    }

    /**
     * @param approved
     *            The approved to set.
     */
    public void setApproved(Integer approved) {
        this.approved = approved;
    }

    /**
     * @return Returns the curricularCourse.
     */
    public ICurricularCourse getCurricularCourse() {
        return curricularCourse;
    }

    /**
     * @param curricularCourse
     *            The curricularCourse to set.
     */
    public void setCurricularCourse(ICurricularCourse curricularCourse) {
        this.curricularCourse = curricularCourse;
    }

    /**
     * @return Returns the curricularYear.
     */
    public String getCurricularYear() {
        return curricularYear;
    }

    /**
     * @param curricularYear
     *            The curricularYear to set.
     */
    public void setCurricularYear(String curricularYear) {
        this.curricularYear = curricularYear;
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
     * @return Returns the keyCurricularCourse.
     */
    public Integer getKeyCurricularCourse() {
        return keyCurricularCourse;
    }

    /**
     * @param keyCurricularCourse
     *            The keyCurricularCourse to set.
     */
    public void setKeyCurricularCourse(Integer keyCurricularCourse) {
        this.keyCurricularCourse = keyCurricularCourse;
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

    public String toString() {
        String result = "[Dominio.gesdis.CourseHistoric ";
        result += ", enrolled=" + getEnrolled();
        result += ", evaluated=" + getEvaluated();
        result += ", approved=" + getApproved();
        result += ", curricularYear=" + getCurricularYear();
        result += ", semester=" + getEnrolled();
        result += ", curricularCourse=" + getCurricularCourse();
        result += "]";
        return result;
    }
}