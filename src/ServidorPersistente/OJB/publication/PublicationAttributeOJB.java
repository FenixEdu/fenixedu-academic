/*
 * Created on 22/Nov/2003
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.publication.Attribute;
import Dominio.publication.PublicationType;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.publication.IPersistentPublicationAttribute;

/**
 * @author TJBF & PFON
 */
public class PublicationAttributeOJB extends ObjectFenixOJB implements IPersistentPublicationAttribute
{

    /**
     * 
     */
    public PublicationAttributeOJB()
    {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAll() throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        return queryList(Attribute.class, criteria);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentPublicationAttribute#readAllByPublicationType(Dominio.teacher.PublicationType)
     */
    public List readAllByPublicationType(PublicationType publicationType) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        //criteria.addEqualTo()
        return null;
    }
}