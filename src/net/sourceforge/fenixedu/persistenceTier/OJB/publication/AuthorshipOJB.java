
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthorship;

import org.apache.ojb.broker.query.Criteria;

public class AuthorshipOJB extends PersistentObjectOJB implements IPersistentAuthorship {

    public List<Authorship> readByPublicationId(int publicationId) throws ExcepcaoPersistencia {
        List<Authorship> result = new ArrayList();
        
        Criteria criteria = new Criteria();
        criteria.addEqualTo("publication.idInternal", publicationId);
        result = (List<Authorship>) queryList(Authorship.class, criteria);
        return result;
    }
    
}