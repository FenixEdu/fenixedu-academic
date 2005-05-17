package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;
import net.sourceforge.fenixedu.util.PeriodState;

import org.apache.ojb.broker.query.Criteria;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
 */
public class ExecutionPeriodOJB extends ObjectFenixOJB implements IPersistentExecutionPeriod {
     
    public ExecutionPeriodOJB() {
        super();
    }

    public List readAllExecutionPeriod() throws ExcepcaoPersistencia {
        return queryList(ExecutionPeriod.class, new Criteria());
    }

    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.CURRENT);
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);

    }

    public IExecutionPeriod readByNameAndExecutionYear(String executionPeriodName,
            String year) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", executionPeriodName);
        crit.addEqualTo("executionYear.year", year);
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, crit);

    }

    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, String year)
            throws ExcepcaoPersistencia {
        if (year == null) {
            return null;
        }

        Criteria criteria = new Criteria();
        criteria.addEqualTo("semester", semester);
        criteria.addEqualTo("executionYear.year", year);
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);
    }

    public List readPublic() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
        criteria.addGreaterThan("semester", new Integer(0));
        return queryList(ExecutionPeriod.class, criteria);
    }

    public List readByExecutionYear(Integer executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYear);
        return queryList(ExecutionPeriod.class, criteria);
    }

    public List readNotClosedExecutionPeriods() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.CLOSED);
        return queryList(ExecutionPeriod.class, criteria);
    }

    public List readNotClosedPublicExecutionPeriods() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.CLOSED);
        criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
        criteria.addGreaterThan("semester", new Integer(0));
        return queryList(ExecutionPeriod.class, criteria);
    }

    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addLessThan("beginDate", end);
        criteria.addGreaterThan("endDate", start);
        return queryList(ExecutionPeriod.class, criteria);
    }
    
	public List readNotClosedPublicExecutionPeriodsByExecutionYears(Integer executionYear) throws ExcepcaoPersistencia {
	    Criteria criteria = new Criteria();
	    criteria.addNotEqualTo("state", PeriodState.CLOSED);
	    criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
	    criteria.addGreaterThan("semester", new Integer(0));
	    criteria.addEqualTo("executionYear.idInternal", executionYear);
	    return queryList(ExecutionPeriod.class, criteria);
	 }
}