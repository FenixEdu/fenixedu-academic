/*
 * Created on 28/Abr/2003 by jpvl
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.Date;

/**
 * @author jpvl
 */
public abstract class EnrolmentPeriod extends DomainObject implements IEnrolmentPeriod {

    protected String ojbConcreteClass;

    private Integer keyDegreeCurricularPlan;

    private Integer keyExecutionPeriod;

    private IDegreeCurricularPlan degreeCurricularPlan;

    private IExecutionPeriod executionPeriod;

    private Date startDate;

    private Date endDate;

    public EnrolmentPeriod() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IEnrolmentPeriodInCurricularCourses#setDegreeCurricularPlan(Dominio.IDegreeCurricularPlan)
     */
    public void setDegreeCurricularPlan(IDegreeCurricularPlan degreeCurricularPlan) {
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IEnrolmentPeriodInCurricularCourses#setExecutionPeriod(Dominio.IExecutionPeriod)
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod) {
        this.executionPeriod = executionPeriod;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IEnrolmentPeriodInCurricularCourses#setStartDate(java.util.Date)
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IEnrolmentPeriodInCurricularCourses#setEndDate(java.util.Date)
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return
     */
    public IDegreeCurricularPlan getDegreeCurricularPlan() {
        return degreeCurricularPlan;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @return
     */
    public IExecutionPeriod getExecutionPeriod() {
        return executionPeriod;
    }

    /**
     * @return
     */
    public Integer getKeyDegreeCurricularPlan() {
        return keyDegreeCurricularPlan;
    }

    /**
     * @return
     */
    public Integer getKeyExecutionPeriod() {
        return keyExecutionPeriod;
    }

    /**
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param integer
     */
    public void setKeyDegreeCurricularPlan(Integer integer) {
        keyDegreeCurricularPlan = integer;
    }

    /**
     * @param integer
     */
    public void setKeyExecutionPeriod(Integer integer) {
        keyExecutionPeriod = integer;
    }

    public String getOjbConcreteClass() {
        return ojbConcreteClass;
    }

    public void setOjbConcreteClass(String ojbConcreteClass) {
        this.ojbConcreteClass = ojbConcreteClass;
    }
}