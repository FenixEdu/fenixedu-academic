/*
 * Created on 22/Nov/2003
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Attribute;
import net.sourceforge.fenixedu.domain.publication.PublicationType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationAttribute;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author TJBF & PFON
 */
public class PublicationAttributeOJB extends PersistentObjectOJB implements
        IPersistentPublicationAttribute {

    /**
     *  
     */
    public PublicationAttributeOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAll() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        return queryList(Attribute.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentPublicationAttribute#readAllByPublicationType(Dominio.teacher.PublicationType)
     */
    public List readAllByPublicationType(PublicationType publicationType) {
        //Criteria criteria = new Criteria();
        //criteria.addEqualTo()
        return null;
    }
}