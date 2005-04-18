/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.gesdis.IStudentCourseReport;
import net.sourceforge.fenixedu.domain.gesdis.StudentCourseReport;

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
    
    public void copyFromDomain(IStudentCourseReport scr) {
        super.copyFromDomain(scr);
        if (scr != null) {
            setStrongPoints(scr.getStrongPoints());
            setWeakPoints(scr.getWeakPoints());
            setStudentReport(scr.getStudentReport());
            setLastModificationDate(scr.getLastModificationDate());
        }
    }
    
    public static InfoStudentCourseReport newInfoFromDomain(IStudentCourseReport scr) {
        InfoStudentCourseReport infoStudentCourseReport = null;
        if (scr != null) {
            infoStudentCourseReport = new InfoStudentCourseReport();
            infoStudentCourseReport.copyFromDomain(scr);
        }
        return infoStudentCourseReport;
    }
    
    public void copyToDomain(InfoStudentCourseReport infoStudentCourseReport,
            IStudentCourseReport studentCourseReport) {
        super.copyToDomain(infoStudentCourseReport, studentCourseReport);

        studentCourseReport.setStrongPoints(infoStudentCourseReport.getStrongPoints());
        studentCourseReport.setWeakPoints(infoStudentCourseReport.getWeakPoints());
        studentCourseReport.setStudentReport(infoStudentCourseReport.getStudentReport());
        studentCourseReport.setLastModificationDate(infoStudentCourseReport.getLastModificationDate());

        if (infoStudentCourseReport.getInfoCurricularCourse() != null) {
            studentCourseReport.setCurricularCourse(InfoCurricularCourse
                    .newDomainFromInfo(infoStudentCourseReport.getInfoCurricularCourse()));
        }
    }
    
    public static IStudentCourseReport newDomainFromInfo(
            InfoStudentCourseReport infoStudentCourseReport) {
        IStudentCourseReport studentCourseReport = null;
        if (infoStudentCourseReport != null) {
            studentCourseReport = new StudentCourseReport();
            infoStudentCourseReport.copyToDomain(infoStudentCourseReport, studentCourseReport);
        }
        return studentCourseReport;
    }

}