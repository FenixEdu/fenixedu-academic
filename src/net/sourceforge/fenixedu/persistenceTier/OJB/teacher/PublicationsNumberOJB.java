/*
 * Created on 21/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 *  
 */
public class PublicationsNumberOJB extends PersistentObjectOJB implements IPersistentPublicationsNumber {

    /**
     *  
     */
    public PublicationsNumberOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentPublicationsNumber#readByTeacherAndPublicationType(Dominio.ITeacher,
     *      Util.PublicationType)
     */
    public IPublicationsNumber readByTeacherAndPublicationType(ITeacher teacher,
            PublicationType publicationType) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher", teacher.getIdInternal());
        criteria.addEqualTo("publicationType", new Integer(publicationType.getValue()));
        return (IPublicationsNumber) queryObject(PublicationsNumber.class, criteria);
    }

}