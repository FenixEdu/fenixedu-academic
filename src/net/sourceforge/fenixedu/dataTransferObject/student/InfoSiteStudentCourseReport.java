/*
 * Created on Feb 25, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.gesdis.InfoSiteEvaluationStatistics;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class InfoSiteStudentCourseReport extends DataTranferObject {
    private InfoStudentCourseReport infoStudentCourseReport;

    private InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    private List infoSiteEvaluationHistory;

    /**
     *  
     */
    public InfoSiteStudentCourseReport() {
        super();
    }

    /**
     * @return Returns the infoSiteEvaluationHistory.
     */
    public List getInfoSiteEvaluationHistory() {
        return infoSiteEvaluationHistory;
    }

    /**
     * @param infoSiteEvaluationHistory
     *            The infoSiteEvaluationHistory to set.
     */
    public void setInfoSiteEvaluationHistory(List infoSiteEvaluationHistory) {
        this.infoSiteEvaluationHistory = infoSiteEvaluationHistory;
    }

    /**
     * @return Returns the infoSiteEvaluationStatistics.
     */
    public InfoSiteEvaluationStatistics getInfoSiteEvaluationStatistics() {
        return infoSiteEvaluationStatistics;
    }

    /**
     * @param infoSiteEvaluationStatistics
     *            The infoSiteEvaluationStatistics to set.
     */
    public void setInfoSiteEvaluationStatistics(InfoSiteEvaluationStatistics infoSiteEvaluationStatistics) {
        this.infoSiteEvaluationStatistics = infoSiteEvaluationStatistics;
    }

    /**
     * @return Returns the infoStudentCourseReport.
     */
    public InfoStudentCourseReport getInfoStudentCourseReport() {
        return infoStudentCourseReport;
    }

    /**
     * @param infoStudentCourseReport
     *            The infoStudentCourseReport to set.
     */
    public void setInfoStudentCourseReport(InfoStudentCourseReport infoStudentCourseReport) {
        this.infoStudentCourseReport = infoStudentCourseReport;
    }
}