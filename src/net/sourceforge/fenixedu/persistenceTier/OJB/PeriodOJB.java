/*
 * Created on 21/Out/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPeriod;
import net.sourceforge.fenixedu.domain.Period;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPeriod;

import org.apache.ojb.broker.query.Criteria;
import org.odmg.QueryException;

/**
 * @author Ana e Ricardo
 *  
 */
public class PeriodOJB extends ObjectFenixOJB implements IPersistentPeriod {

    public List readBy(Calendar startDate) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("startDate", startDate);
        return queryList(Period.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        try {
            String oqlQuery = "select all from " + Period.class.getName();
            //			oqlQuery += " order by season asc";
            query.create(oqlQuery);
            List result = (List) query.execute();
            lockRead(result);
            return result;
        } catch (QueryException ex) {
            throw new ExcepcaoPersistencia(ExcepcaoPersistencia.QUERY, ex);
        }
    }

    public void delete(IPeriod period) throws ExcepcaoPersistencia {
        // TO DO falta apagar as ligações a outras tabelas
        super.delete(period);

    }

    public void deleteAll() throws ExcepcaoPersistencia {
        String oqlQuery = "select all from " + Period.class.getName();
        super.deleteAll(oqlQuery);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.IPersistentPeriod#readBy(java.util.Calendar,
     *      java.util.Calendar)
     */
    public Object readByCalendarAndNextPeriod(Calendar startDate, Calendar endDate, Integer keyNextPeriod)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("startDate", startDate);
        criteria.addEqualTo("endDate", endDate);
        if (keyNextPeriod != null) {
            criteria.addEqualTo("nextPeriod.idInternal", keyNextPeriod);
        }
        return queryObject(Period.class, criteria);
    }

}