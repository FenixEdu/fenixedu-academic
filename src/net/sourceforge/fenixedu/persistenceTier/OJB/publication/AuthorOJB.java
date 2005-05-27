/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.Author;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentAuthor;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author TJBF & PFON
 *  
 */
public class AuthorOJB extends PersistentObjectOJB implements IPersistentAuthor {

    /**
     *  
     */
    public AuthorOJB() {
        super();
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public Author readAuthorByKeyPerson(Integer keyPerson) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", keyPerson);
        Author author = (Author) queryObject(Author.class, criteria);
        return author;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.publication.IPersistentAuthor#readAuthorsBySubName(java.lang.String)
     */
    public List readAuthorsBySubName(String stringSubName) throws ExcepcaoPersistencia {

        List authors = new ArrayList();

        Criteria criteria = new Criteria();
        criteria.addLike("author", stringSubName);

        authors = queryList(Author.class, criteria);

        return authors;
    }
}