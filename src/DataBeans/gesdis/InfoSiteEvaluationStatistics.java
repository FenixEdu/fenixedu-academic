/*
 * Created on Feb 13, 2004
 *
 */
package DataBeans.gesdis;

import DataBeans.DataTranferObject;
import DataBeans.InfoExecutionPeriod;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class InfoSiteEvaluationStatistics extends DataTranferObject {
    private Integer enrolled;

    private Integer evaluated;

    private Integer approved;

    private InfoExecutionPeriod infoExecutionPeriod;

    /**
     *  
     */
    public InfoSiteEvaluationStatistics() {
        super();
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
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

}