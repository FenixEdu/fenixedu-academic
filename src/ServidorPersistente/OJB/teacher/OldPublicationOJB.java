/*
 * Created on 22/Nov/2003
 *
 */
package ServidorPersistente.OJB.teacher;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.teacher.OldPublication;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.teacher.IPersistentOldPublication;
import Util.OldPublicationType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class OldPublicationOJB extends PersistentObjectOJB implements IPersistentOldPublication {

    /**
     *  
     */
    public OldPublicationOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAllByTeacherAndOldPublicationType(ITeacher teacher,
            OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("oldPublicationType", new Integer(oldPublicationType.getValue()));
        return queryList(OldPublication.class, criteria);
    }

}