package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PublicationType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class PublicationsNumberVO extends VersionedObjectsBase implements IPersistentPublicationsNumber {

    public IPublicationsNumber readByTeacherAndPublicationType(final ITeacher teacher,
            final PublicationType publicationType) throws ExcepcaoPersistencia {
        
        final List<PublicationsNumber> publicationsNumbers = (List<PublicationsNumber>) readAll(PublicationsNumber.class);
        
        return (IPublicationsNumber) CollectionUtils.find(publicationsNumbers,new Predicate() {

            public boolean evaluate(Object object) {
                final IPublicationsNumber publicationsNumber = (IPublicationsNumber)object;
                if (publicationsNumber.getTeacher().getIdInternal().equals(teacher.getIdInternal())
                        && publicationsNumber.getPublicationType().equals(new Integer(publicationType.getValue()))) {
                    return true;
                }
                return false;
            }
            
        });

    }

}