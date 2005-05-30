package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.query.Criteria;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
 */
public class ExecutionYearOJB extends PersistentObjectOJB implements IPersistentExecutionYear {

    public ExecutionYearOJB() {
        super();
    }

    public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }

    public List readExecutionYearsInPeriod(Date start, Date end) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();

        criteria.addLessThan("beginDate", end);
        criteria.addGreaterThan("endDate", start);
        return queryList(ExecutionYear.class, criteria);        
    }
    
    public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.CURRENT);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }


    public List readNotClosedExecutionYears() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.CLOSED);
        return queryList(ExecutionYear.class, criteria);
    }

    public List readOpenExecutionYears() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.OPEN);
        return queryList(ExecutionYear.class, criteria);
    }
}