package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularYear;

/**
 * @author dcs-rjao 20/Mar/2003
 */

public class CurricularYearOJB extends ObjectFenixOJB implements IPersistentCurricularYear
{

    public CurricularYearOJB()
    {
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