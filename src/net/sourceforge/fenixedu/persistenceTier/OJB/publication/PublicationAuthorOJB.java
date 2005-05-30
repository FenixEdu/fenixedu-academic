/*
 * Created on 27/Out/2004
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

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
}
