package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CursoExecucao;
import Dominio.ExecutionPeriod;
import Dominio.ExecutionYear;
import Dominio.IExecutionYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionYear;
import ServidorPersistente.exceptions.ExistingPersistentException;
import Util.PeriodState;

/**
 * Created on 11/Fev/2003
 * 
 * @author João Mota Package ServidorPersistente.OJB
 */
public class ExecutionYearOJB extends ObjectFenixOJB implements IPersistentExecutionYear
{

    /**
	 * Constructor for ExecutionYearOJB.
	 */
    public ExecutionYearOJB()
    {
        super();
    }
    /**
	 * @see ServidorPersistente.IPersistentExecutionYear#readExecutionYearByName(java.lang.String)
	 */
    public IExecutionYear readExecutionYearByName(String year) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("year", year);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }

    /**
	 * @see ServidorPersistente.IPersistentExecutionYear#readAllExecutionYear()
	 */
    public List readAllExecutionYear() throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addOrderBy("year", false);
        return queryList(ExecutionYear.class, crit);

    }
    /**
	 * @see ServidorPersistente.IPersistentExecutionYear#writeExecutionYear(Dominio.IExecutionYear)
	 */
    public void writeExecutionYear(IExecutionYear executionYearToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        IExecutionYear executionYearFromDB = null;

        // If there is nothing to write, simply return.
        if (executionYearToWrite == null)
            return;

        // Read execution Year from database.
        executionYearFromDB = this.readExecutionYearByName(executionYearToWrite.getYear());

        // If execution Year is not in database, then write it.
        if (executionYearFromDB == null)
            super.lockWrite(executionYearToWrite);
        // else If the execution Year is mapped to the database, then write any
        // existing changes.
        else if (
            (executionYearToWrite instanceof ExecutionYear)
                && executionYearFromDB.getIdInternal().equals(executionYearToWrite.getIdInternal()))
        {
            super.lockWrite(executionYearToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public boolean delete(IExecutionYear executionYear)
    {
        try
        {
            Criteria crit1 = new Criteria();
            crit1.addEqualTo("executionYear.year", executionYear.getYear());

            List executionPeriods = queryList(ExecutionPeriod.class, crit1);
            Criteria crit2 = new Criteria();
            crit2.addEqualTo("executionYear.year", executionYear.getYear());

            List executionDegrees = queryList(CursoExecucao.class, crit2);

            if ((executionPeriods == null || executionPeriods.isEmpty())
                && (executionDegrees == null || executionDegrees.isEmpty()))
            {

                super.delete(executionYear);
            }
            else
            {

                return false;
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public IExecutionYear readCurrentExecutionYear() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("state", PeriodState.CURRENT);
        return (IExecutionYear) queryObject(ExecutionYear.class, criteria);
    }
    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.IPersistentExecutionYear#readNotClosedExecutionYears()
	 */
    public List readNotClosedExecutionYears() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("state", PeriodState.CLOSED);
        return queryList(ExecutionYear.class, criteria);
    }
}
