/*
 * Created on 17/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.gesdis.ICourseHistoric;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class InfoCourseHistoric extends InfoObject {

    private Integer enrolled;

    private Integer evaluated;

    private Integer approved;

    private String curricularYear;

    private Integer semester;

    private InfoCurricularCourse infoCurricularCourse;

    public InfoCourseHistoric() {
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

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoObject#copyFromDomain(Dominio.IDomainObject)
     */
    public void copyFromDomain(ICourseHistoric courseHistoric) {
        super.copyFromDomain(courseHistoric);
        if (courseHistoric != null) {
            setApproved(courseHistoric.getApproved());
            setEnrolled(courseHistoric.getEnrolled());
            setEvaluated(courseHistoric.getEvaluated());
            setCurricularYear(courseHistoric.getCurricularYear());
            setSemester(courseHistoric.getSemester());
        }
    }

    public static InfoCourseHistoric newInfoFromDomain(ICourseHistoric courseHistoric) {
        InfoCourseHistoric infoCourseHistoric = null;
        if (courseHistoric != null) {
            infoCourseHistoric = new InfoCourseHistoric();
            infoCourseHistoric.copyFromDomain(courseHistoric);
        }
        return infoCourseHistoric;
    }
}