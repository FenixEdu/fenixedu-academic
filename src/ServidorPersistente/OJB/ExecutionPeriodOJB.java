package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ExecutionPeriod;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
 */
public class ExecutionPeriodOJB extends ObjectFenixOJB implements IPersistentExecutionPeriod {

    /**
     * Constructor for ExecutionPeriodOJB.
     */
    public ExecutionPeriodOJB() {
        super();
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionPeriod#readAllExecutionPeriod()
     */
    public List readAllExecutionPeriod() throws ExcepcaoPersistencia {
        return queryList(ExecutionPeriod.class, new Criteria());
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionPeriod#writeExecutionPeriod(Dominio.IExecutionPeriod)
     */

    /**
     * @see ServidorPersistente.IPersistentExecutionPeriod#delete(Dominio.IExecutionPeriod)
     */
    public boolean delete(IExecutionPeriod executionPeriod) {
        List executionCourses = new ArrayList();
        List classes = new ArrayList();
        try {

            executionCourses = SuportePersistenteOJB.getInstance().getIPersistentExecutionCourse()
                    .readByExecutionPeriod(executionPeriod);
            classes = SuportePersistenteOJB.getInstance().getITurmaPersistente().readByExecutionPeriod(
                    executionPeriod);

            if (classes.isEmpty() && executionCourses.isEmpty()) {
                super.delete(executionPeriod);
            } else
                return false;

        } catch (ExcepcaoPersistencia e) {
            return false;
        }
        return true;
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionPeriod#readActualExecutionPeriod()
     */
    public IExecutionPeriod readActualExecutionPeriod() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.CURRENT);
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);

    }

    /**
     * @see ServidorPersistente.IPersistentExecutionPeriod#readByNameAndExecutionYear(java.lang.String,
     *      Dominio.IExecutionYear)
     */
    public IExecutionPeriod readByNameAndExecutionYear(String executionPeriodName,
            IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria crit = new Criteria();
        crit.addEqualTo("name", executionPeriodName);
        crit.addEqualTo("executionYear.year", executionYear.getYear());
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, crit);

    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionPeriod#readBySemesterAndExecutionYear(java.lang.String,
     *      Dominio.IExecutionYear)
     */
    public IExecutionPeriod readBySemesterAndExecutionYear(Integer semester, IExecutionYear year)
            throws ExcepcaoPersistencia {
        if (year == null) {
            return null;
        }

        Criteria criteria = new Criteria();
        criteria.addEqualTo("semester", semester);
        criteria.addEqualTo("executionYear.year", year.getYear());
        return (IExecutionPeriod) queryObject(ExecutionPeriod.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionPeriod#readPublic()
     */
    public List readPublic() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
        criteria.addGreaterThan("semester", new Integer(0));
        return queryList(ExecutionPeriod.class, criteria);
    }

    public List readByExecutionYear(IExecutionYear executionYear) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
        return queryList(ExecutionPeriod.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionPeriod#readNotClosedExecutionPeriods()
     */
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

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionPeriod#readExecutionPeriodsInTimePeriod(java.util.Date,
     *      java.util.Date)
     */
    public List readExecutionPeriodsInTimePeriod(Date start, Date end) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();

        criteria.addLessThan("beginDate", end);
        criteria.addGreaterThan("endDate", start);
        return queryList(ExecutionPeriod.class, criteria);
    }
    
	public List readNotClosedPublicExecutionPeriodsByExecutionYears(IExecutionYear executionYear) throws ExcepcaoPersistencia {
		   Criteria criteria = new Criteria();
		   criteria.addNotEqualTo("state", PeriodState.CLOSED);
		   criteria.addNotEqualTo("state", PeriodState.NOT_OPEN);
		   criteria.addGreaterThan("semester", new Integer(0));
		   criteria.addEqualTo("executionYear.idInternal", executionYear.getIdInternal());
		   return queryList(ExecutionPeriod.class, criteria);
	 }
}