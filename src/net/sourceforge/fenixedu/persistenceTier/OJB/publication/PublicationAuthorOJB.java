/*
 * Created on 27/Out/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.PublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;

import org.apache.ojb.broker.query.Criteria;

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
    
    public IPublicationAuthor readByAuthorIdAndPublicationID(Integer authorId, Integer publicationId) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPublication",publicationId);
        criteria.addEqualTo("keyAuthor",authorId);
        return (IPublicationAuthor) queryList(PublicationAuthor.class,criteria).get(0);
    }
    
    public void deleteAllByPublicationID(Integer publicationId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPublication", publicationId);
        List deleteList = queryList(PublicationAuthor.class,criteria);
        Iterator it = deleteList.iterator();
        while (it.hasNext()){
            delete((IPublicationAuthor)it.next());
        }
    }
    
}
