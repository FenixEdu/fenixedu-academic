/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.domain.publication.IAuthor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author TJBF & PFON
 *  
 */
public class AuthorOJB extends PersistentObjectOJB implements IPersistentAuthor {

    public IAuthor readAuthorByKeyPerson(Integer keyPerson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", keyPerson);
        return (IAuthor) queryObject(Author.class, criteria);
    }


    public List readAuthorsBySubName(String stringSubName) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addLike("author", stringSubName);
        return queryList(Author.class, criteria);
    }
}