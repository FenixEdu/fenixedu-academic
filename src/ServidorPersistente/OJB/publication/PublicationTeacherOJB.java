/*
 * Created on 18/Nov/2004
 *
 */
package ServidorPersistente.OJB.publication;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.ITeacher;
import Dominio.publication.IPublication;
import Dominio.publication.IPublicationTeacher;
import Dominio.publication.PublicationTeacher;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.OJB.ObjectFenixOJB;
import ServidorPersistente.publication.IPersistentPublicationTeacher;
import Util.PublicationArea;

/**
 * @author Ricardo Rodrigues
 *
 */
public class PublicationTeacherOJB extends ObjectFenixOJB implements IPersistentPublicationTeacher{

	public List readByPublicationId(Integer publicationId) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyPublication",publicationId);
        return queryList(PublicationTeacher.class,criteria);
    }

    public List readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher",teacherId);
        return queryList(PublicationTeacher.class,criteria);
    }
    
    public IPublicationTeacher readByTeacherAndPublication(ITeacher teacher,
			IPublication publication) throws ExcepcaoPersistencia{
    	Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher",teacher.getIdInternal());
        criteria.addEqualTo("keyPublication",publication.getIdInternal());
    	return (IPublicationTeacher) queryObject(PublicationTeacher.class, criteria);
    }
    
    public List readByTeacherAndPublicationArea(ITeacher teacher, PublicationArea publicationArea)
            throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTeacher",teacher.getIdInternal());
        criteria.addEqualTo("publicationArea",publicationArea);
        return queryList(PublicationTeacher.class,criteria);
    }

}
