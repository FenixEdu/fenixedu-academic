package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.IPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentPublicationsNumber;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.PublicationType;

public class PublicationsNumberVO extends VersionedObjectsBase implements IPersistentPublicationsNumber {

    public IPublicationsNumber readByTeacherIdAndPublicationType(final Integer teacherId,
            final PublicationType publicationType) throws ExcepcaoPersistencia {
        		
		ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherId);
		for(IPublicationsNumber publicationsNumber : teacher.getAssociatedPublicationsNumbers()) {
			if(publicationsNumber.getPublicationType().equals(publicationType))
				return publicationsNumber;
		}
		return null;
    }

}