/*
 * Created on 27/Out/2004
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.publication.PublicationAuthor;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentPublicationAuthor;
import ServidorPersistente.OJB.ObjectFenixOJB;

/**
 * @author Ricardo Rodrigues
 */
public class PublicationAuthorOJB extends ObjectFenixOJB implements IPersistentPublicationAuthor{
        
    
    public List readByPublicationId(Integer publicationId) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPublication",publicationId);
        return queryList(PublicationAuthor.class,criteria);
    }

    public List readByAuthorId(Integer authorId) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyAuthor",authorId);
        return queryList(PublicationAuthor.class,criteria);
    }

}
