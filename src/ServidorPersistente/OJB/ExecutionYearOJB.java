package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
 */
public class ExecutionYearOJB extends PersistentObjectOJB implements IPersistentExecutionYear {

    /**
     * Constructor for ExecutionYearOJB.
     */
    public ExecutionYearOJB() {
        super();
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionYear#readExecutionYearByName(java.lang.String)
     */
    public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }

    /**
     * @see ServidorPersistente.IPersistentExecutionYear#readAllExecutionYear()
     */
    public List readAllExecutionYear() throws ExcepcaoPersistencia {
        return queryList(ExecutionYear.class, null, "year", false);
    }

    public boolean delete(IExecutionYear executionYear) {
        try {
            Criteria crit1 = new Criteria();
            crit1.addEqualTo("executionYear.year", executionYear.getYear());

            List executionPeriods = queryList(ExecutionPeriod.class, crit1);
            Criteria crit2 = new Criteria();
            crit2.addEqualTo("executionYear.year", executionYear.getYear());

            List executionDegrees = queryList(CursoExecucao.class, crit2);

            if ((executionPeriods == null || executionPeriods.isEmpty())
                    && (executionDegrees == null || executionDegrees.isEmpty())) {

                super.delete(executionYear);
            } else {

                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.CURRENT);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentExecutionYear#readNotClosedExecutionYears()
     */
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