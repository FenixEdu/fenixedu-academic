/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package Dominio;

import java.util.Date;

/**
 * @author jpvl
 */
public interface IEnrolmentPeriod extends IDomainObject {

    /**
     * @param degreeCurricularPlan
     */
    void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan);

    IDegreeCurricularPlan getDegreeCurricularPlan();

    /**
     * @param executionPeriod
     */
    void setExecutionPeriod(IExecutionPeriod executionPeriod);

    IExecutionPeriod getExecutionPeriod();

    /**
     * @param startDate
     */
    void setStartDate(Date startDate);

    Date getStartDate();

    /**
     * @param endDate
     */
    void setEndDate(Date endDate);

    Date getEndDate();

}