/*
 * Created on Feb 18, 2004
 *  
 */
package DataBeans.student;

import java.util.Date;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoObject;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class InfoStudentCourseReport extends InfoObject {
    private String strongPoints;

    private String weakPoints;

    private String studentReport;

    private Date lastModificationDate;

    private InfoCurricularCourse infoCurricularCourse;

    /**
     *  
     */
    public InfoStudentCourseReport() {
        super();
    }

    /**
     * @param idInternal
     */
    public InfoStudentCourseReport(Integer idInternal) {
        super(idInternal);
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
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate() {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate
     *            The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    /**
     * @return Returns the strongPoints.
     */
    public String getStrongPoints() {
        return strongPoints;
    }

    /**
     * @param strongPoints
     *            The strongPoints to set.
     */
    public void setStrongPoints(String strongPoints) {
        this.strongPoints = strongPoints;
    }

    /**
     * @return Returns the studentReport.
     */
    public String getStudentReport() {
        return studentReport;
    }

    /**
     * @param studentReport
     *            The studentReport to set.
     */
    public void setStudentReport(String studentReport) {
        this.studentReport = studentReport;
    }

    /**
     * @return Returns the weakPoints.
     */
    public String getWeakPoints() {
        return weakPoints;
    }

    /**
     * @param weakPoints
     *            The weakPoints to set.
     */
    public void setWeakPoints(String weakPoints) {
        this.weakPoints = weakPoints;
    }

}