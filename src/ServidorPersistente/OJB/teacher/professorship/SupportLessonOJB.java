/*
 * Created on Nov 23, 2003 by jpvl
 *  
 */
package ServidorPersistente.OJB.teacher.professorship;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IProfessorship;
import Dominio.ISupportLesson;
import Dominio.SupportLesson;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.teacher.professorship.IPersistentSupportLesson;

/**
 * @author jpvl
 */
public class SupportLessonOJB extends ObjectFenixOJB implements IPersistentSupportLesson
{

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByProfessorship(Dominio.IProfessorship)
	 */
    public List readByProfessorship(IProfessorship professorship) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", professorship.getIdInternal());
        return queryList(SupportLesson.class, criteria);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see ServidorPersistente.teacher.professorship.IPersistentSupportLesson#readByUnique(Dominio.ISupportLesson)
	 */
    public ISupportLesson readByUnique(ISupportLesson supportLesson) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyProfessorship", supportLesson.getProfessorship().getIdInternal());
        criteria.addEqualTo("weekDay", supportLesson.getWeekDay());
        criteria.addEqualTo("startTime", supportLesson.getStartTime());
        criteria.addEqualTo("endTime", supportLesson.getEndTime());
        return (ISupportLesson) queryObject(SupportLesson.class, criteria);
    }

}
