/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.util.OldPublicationType;

import org.apache.ojb.broker.query.Criteria;

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
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.Teacher,
     *      Util.OldPublicationType)
     */
    public List readAllByTeacherIdAndOldPublicationType(final Integer teacherId,
            final OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyOrientationTeacher", teacherId);
        criteria.addEqualTo("oldPublicationType", new Integer(oldPublicationType.getValue()));
        return queryList(OldPublication.class, criteria);
    }

}