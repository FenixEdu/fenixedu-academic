/*
 * Created on Feb 12, 2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.gesdis;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class InfoSiteEvaluationInformation extends DataTranferObject {
    private InfoCurricularCourse infoCurricularCourse;

    private InfoSiteEvaluationStatistics infoSiteEvaluationStatistics;

    private List infoSiteEvaluationHistory;

    /**
     *  
     */
    public InfoSiteEvaluationInformation() {
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

}