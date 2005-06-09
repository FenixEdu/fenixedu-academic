package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.publication.IPersistentPublicationTeacher;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PublicationArea;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class PublicationTeacherVO extends VersionedObjectsBase implements IPersistentPublicationTeacher{

    public IPublicationTeacher readByTeacherAndPublication(final ITeacher teacher,
			final IPublication publication) throws ExcepcaoPersistencia{

        final List<IPublicationTeacher> publicationTeachers = (List<IPublicationTeacher>) readAll(PublicationTeacher.class);

    	return (IPublicationTeacher)CollectionUtils.find(publicationTeachers,new Predicate() {

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
    
    public List readByTeacherAndPublicationArea(final ITeacher teacher, final PublicationArea publicationArea)
            throws ExcepcaoPersistencia{

        final List<IPublicationTeacher> publicationTeachers = (List<IPublicationTeacher>) readAll(PublicationTeacher.class);

        return (List<IPublicationTeacher>)CollectionUtils.select(publicationTeachers,new Predicate() {

            public boolean evaluate(Object object) {
                final IPublicationTeacher publicationTeacher = (IPublicationTeacher) object;
                if (publicationTeacher.getTeacher().getIdInternal().equals(teacher.getIdInternal()) &&
                        publicationTeacher.getPublicationArea().equals(publicationArea)) {
                    return true;
                }
                return false;
            }

        });
        
    }

}
