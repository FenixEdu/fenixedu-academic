/*
 * Created on 22/Nov/2003
 *
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.publication.PublicationType;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.publication.IPersistentPublicationType;

/**
 * @author TJBF & PFON
 *
 */
public class PublicationTypeOJB extends ObjectFenixOJB implements IPersistentPublicationType
{

    /**
     * 
     */
    public PublicationTypeOJB()
    {
        super();
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher, Util.OldPublicationType)
     */
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(PublicationType.class, criteria);
    }
}
