package Dominio;

import java.util.Date;

import Util.PeriodState;
import fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 *  
 */
public interface IExecutionPeriod extends IDomainObject, INode {

    public String getName();

    public IExecutionYear getExecutionYear();

    public void setExecutionYear(IExecutionYear executionYear);

    public void setName(String name);

    void setState(PeriodState newState);

    PeriodState getState();

    Integer getSemester();

    void setSemester(Integer semester);

    Date getBeginDate();

    void setBeginDate(Date beginDate);

    Date getEndDate();

    void setEndDate(Date endDate);

    /**
     * @return Returns the previousExecutionPeriod.
     */
    public IExecutionPeriod getPreviousExecutionPeriod();

    /**
     * @param previousExecutionPeriod
     *            The previousExecutionPeriod to set.
     */
    public void setPreviousExecutionPeriod(IExecutionPeriod previousExecutionPeriod);
}