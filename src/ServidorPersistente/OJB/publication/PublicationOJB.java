/*
 * Created on 22/Nov/2003
 *
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.Query;
import org.apache.ojb.broker.query.QueryByCriteria;

import Dominio.IPessoa;
import Dominio.publication.Publication;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.PersistentObjectOJB;
import ServidorPersistente.publication.IPersistentPublication;

/**
 * @author TJBF & PFON
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
     * (non-Javadoc)
     * 
     * @see ServidorPersistente.teacher.IPersistentOldPublication#readAllByTeacherAndOldPublicationType(Dominio.ITeacher,
     *      Util.OldPublicationType)
     */
    public List readAllByPersonAndPublicationType(IPessoa pessoa, Integer publicationType)
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