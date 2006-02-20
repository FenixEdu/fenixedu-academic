/*
 * Created on 18/Nov/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.publication;

import java.util.List;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
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

    public PublicationTeacher readByTeacherAndPublication(Teacher teacher,
			Publication publication) throws ExcepcaoPersistencia{
    	Criteria criteria = new Criteria();
        criteria.addEqualTo("keyOrientationTeacher",teacher.getIdInternal());
        criteria.addEqualTo("keyPublication",publication.getIdInternal());
    	return (PublicationTeacher) queryObject(PublicationTeacher.class, criteria);
    }
    
    public List readByTeacherAndPublicationArea(Teacher teacher, PublicationArea publicationArea)
            throws ExcepcaoPersistencia{
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyOrientationTeacher",teacher.getIdInternal());
        criteria.addEqualTo("publicationArea",publicationArea);
        return queryList(PublicationTeacher.class,criteria);
    }

}
