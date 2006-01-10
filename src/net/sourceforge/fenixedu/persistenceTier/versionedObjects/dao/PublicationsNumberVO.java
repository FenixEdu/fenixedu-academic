package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.PublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PublicationType;

public class PublicationsNumberVO extends VersionedObjectsBase implements IPersistentPublicationsNumber {

    public PublicationsNumber readByTeacherIdAndPublicationType(final Integer teacherId,
            final PublicationType publicationType) throws ExcepcaoPersistencia {
        		
		Teacher teacher = (Teacher) readByOID(Teacher.class, teacherId);
		for(PublicationsNumber publicationsNumber : teacher.getAssociatedPublicationsNumbers()) {
			if(publicationsNumber.getPublicationType().equals(publicationType))
				return publicationsNumber;
		}
		return null;
    }

}