/*
 * Created on Dec 26, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.credits;

import org.apache.ojb.broker.query.Criteria;

import Dominio.Credits;
import Dominio.ICredits;
import Dominio.IExecutionPeriod;
import Dominio.ITeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.credits.IPersistentCredits;

/**
 * @author jpvl
 */
public class CreditsOJB extends ObjectFenixOJB implements IPersistentCredits
{

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.credits.IPersistentCredits#readByTeacherAndExecutionPeriod(Dominio.ITeacher,
     *          Dominio.IExecutionPeriod)
     */
    public ICredits readByTeacherAndExecutionPeriod( ITeacher teacher, IExecutionPeriod executionPeriod )
                    throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("teacher.idInternal", teacher.getIdInternal());
        criteria.addEqualTo("executionPeriod.idInternal", executionPeriod.getIdInternal());
        return (ICredits) queryObject(Credits.class, criteria);
    }

}
