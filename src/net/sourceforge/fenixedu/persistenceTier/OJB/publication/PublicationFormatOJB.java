/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.PublicationFormat;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationFormat;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author TJBF & PFON
 *  
 */
public class PublicationFormatOJB extends PersistentObjectOJB implements IPersistentPublicationFormat {

    /**
     *  
     */
    public PublicationFormatOJB() {
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
        return queryList(PublicationFormat.class, criteria);
    }
}