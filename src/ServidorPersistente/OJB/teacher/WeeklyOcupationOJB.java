/*
 * Created on 15/Nov/2003
 *
 */
package ServidorPersistente.OJB.teacher;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.IWeeklyOcupation;
import Dominio.teacher.WeeklyOcupation;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.IPersistentWeeklyOcupation;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *
 */
public class WeeklyOcupationOJB extends ObjectFenixOJB implements IPersistentWeeklyOcupation
{

    /**
     * 
     */
    public WeeklyOcupationOJB()
    {
        super();
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.IPersistentWeeklyOcupation#readByTeacher(Dominio.ITeacher)
     */
    public IWeeklyOcupation readByTeacher(ITeacher teacher) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        return (IWeeklyOcupation) queryObject(WeeklyOcupation.class, criteria);
    }


}
