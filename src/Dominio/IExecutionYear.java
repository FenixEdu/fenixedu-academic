package Dominio;

import java.util.Date;
import java.util.List;

import Util.PeriodState;
import fileSuport.INode;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota ciapl Dominio
 *  
 */
public interface IExecutionYear extends IDomainObject, INode {
    public String getYear();

    public void setYear(String year);

    public void setState(PeriodState state);

    public PeriodState getState();

    public Date getBeginDate();

    public void setBeginDate(Date beginDate);

    public Date getEndDate();

    public void setEndDate(Date endDate);

    public List getExecutionPeriods();

    public void setExecutionPeriods(List executionPeriods);
}