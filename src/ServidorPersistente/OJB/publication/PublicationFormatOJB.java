/*
 * Created on 22/Nov/2003
 *
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.publication.PublicationFormat;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.publication.IPersistentPublicationFormat;

/**
 * @author TJBF & PFON
 *
 */
public class PublicationFormatOJB extends ObjectFenixOJB implements IPersistentPublicationFormat
{

    /**
     * 
     */
    public PublicationFormatOJB()
    {
        super();
    }

    /* (non-Javadoc)
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher, Util.OldPublicationType)
     */
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(PublicationFormat.class, criteria);
    }
}
