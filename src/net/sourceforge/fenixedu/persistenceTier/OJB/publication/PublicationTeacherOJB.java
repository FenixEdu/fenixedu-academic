/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.ObjectFenixOJB;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

import org.apache.ojb.broker.query.Criteria;

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
