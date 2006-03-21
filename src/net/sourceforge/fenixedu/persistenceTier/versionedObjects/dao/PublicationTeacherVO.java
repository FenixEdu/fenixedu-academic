package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.research.result.Publication;
import net.sourceforge.fenixedu.domain.research.result.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PublicationArea;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class PublicationTeacherVO extends VersionedObjectsBase implements IPersistentPublicationTeacher{

    public PublicationTeacher readByTeacherAndPublication(final Teacher teacher,
			final Publication publication) throws ExcepcaoPersistencia{

        final List<PublicationTeacher> publicationTeachers = (List<PublicationTeacher>) readAll(PublicationTeacher.class);

    	return (PublicationTeacher)CollectionUtils.find(publicationTeachers,new Predicate() {

            public boolean evaluate(Object object) {
                final PublicationTeacher publicationTeacher = (PublicationTeacher) object;
                if (publicationTeacher.getTeacher().getIdInternal().equals(teacher.getIdInternal()) &&
                        publicationTeacher.getPublication().getIdInternal().equals(publication.getIdInternal())) {
                    return true;
                }
                return false;
            }

        });

    }
    
    public List readByTeacherAndPublicationArea(final Teacher teacher, final PublicationArea publicationArea)
            throws ExcepcaoPersistencia{

        final List<PublicationTeacher> publicationTeachers = (List<PublicationTeacher>) readAll(PublicationTeacher.class);

        return (List<PublicationTeacher>)CollectionUtils.select(publicationTeachers,new Predicate() {

            public boolean evaluate(Object object) {
                final PublicationTeacher publicationTeacher = (PublicationTeacher) object;
                if (publicationTeacher.getTeacher().getIdInternal().equals(teacher.getIdInternal()) &&
                        publicationTeacher.getPublicationArea().equals(publicationArea)) {
                    return true;
                }
                return false;
            }

        });
        
    }

}
