package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author dcs-rjao
 * 
 * 20/Mar/2003
 */

public class CurricularYearOJB extends ObjectFenixOJB implements IPersistentCurricularYear
{

    public CurricularYearOJB()
    {
    }

    public void lockWrite(ICurricularYear curricularYearToWrite)
        throws ExcepcaoPersistencia, ExistingPersistentException
    {

        ICurricularYear curricularYearFromDB = null;

        // If there is nothing to write, simply return.
        if (curricularYearToWrite == null)
        {
            return;
        }

        // Read CurricularYear from database.
        curricularYearFromDB = this.readCurricularYearByYear(curricularYearToWrite.getYear());

        // If CurricularYear is not in database, then write it.
        if (curricularYearFromDB == null)
        {
            super.lockWrite(curricularYearToWrite);
            // else If the CurricularYear is mapped to the database, then write
            // any existing changes.
        }
        else if (
            (curricularYearToWrite instanceof CurricularYear)
                && ((CurricularYear) curricularYearFromDB).getIdInternal().equals(
                    ((CurricularYear) curricularYearToWrite).getIdInternal()))
        {
            super.lockWrite(curricularYearToWrite);
            // else Throw an already existing exception
        }
        else
            throw new ExistingPersistentException();
    }

    public void delete(ICurricularYear curricularYear) throws ExcepcaoPersistencia
    {
        try
        {
            super.delete(curricularYear);
        }
        catch (ExcepcaoPersistencia ex)
        {
            throw ex;
        }
    }

    public ICurricularYear readCurricularYearByYear(Integer year) throws ExcepcaoPersistencia
    {
        Criteria crit = new Criteria();
        crit.addEqualTo("year", year);
        return (ICurricularYear) queryObject(CurricularYear.class, crit);

    }

    public List readAll() throws ExcepcaoPersistencia
    {
        return queryList(CurricularYear.class, new Criteria());

    }

}