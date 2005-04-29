/*
 * Created on 22/Nov/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationAuthor;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentPublicationAuthor;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublication;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author TJBF & PFON
 * @author Carlos Pereira & Francisco Passos
 *  
 */
public class PublicationOJB extends PersistentObjectOJB implements IPersistentPublication {

    /**
     *  
     */
    public PublicationOJB() {
        super();
    }

    /*
     * This lockWrite methor not only writes the Publication itself, but also the
     * Publication_Authors associated with it  
     */    
    public void lockWrite(Object obj) throws ExcepcaoPersistencia {
        super.lockWrite(obj);
        IPublication publication = (IPublication) obj;
        IPersistentPublicationAuthor ppa = new PublicationAuthorOJB();
        
        Iterator it = publication.getPublicationAuthors().iterator();
        while (it.hasNext()){
            IPublicationAuthor pa = (IPublicationAuthor) it.next();
            ppa.lockWrite(pa);
        }
    }
    
    /*
     * This delete method not only deletes the Publication itself, but also the
     * Publication_Authors associated with it  
     */
    public void delete(Object obj) throws ExcepcaoPersistencia{
        super.delete(obj);
        IPublication publication = (IPublication) obj;
        IPersistentPublicationAuthor ppa = new PublicationAuthorOJB();
        
        Iterator it = publication.getPublicationAuthors().iterator();
        while (it.hasNext()){
            IPublicationAuthor pa = (IPublicationAuthor) it.next();
            ppa.deleteByOID(pa.getClass(),pa.getIdInternal());
        }        
    }
        
    /*
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAllByPersonAndPublicationType(IPerson pessoa, Integer publicationType)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPerson", pessoa.getIdInternal());
        criteria.addEqualTo("didatic", publicationType);
        List lista = queryList(Publication.class, criteria);
        return lista;
    }

    public List readAll() throws ExcepcaoPersistencia {
        Query query = new QueryByCriteria(Publication.class);
        List result = queryList(query);
        return result;
    }

    public List readByPublicationsTypeId(Integer publicationTypeId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPublicationType", publicationTypeId);
        return queryList(Publication.class, criteria);
    }
}