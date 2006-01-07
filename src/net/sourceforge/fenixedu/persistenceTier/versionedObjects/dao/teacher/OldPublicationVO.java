/*
 * Created on 13/Set/2005 - 14:02:33
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import java.util.List;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.domain.teacher.OldPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOldPublication;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.OldPublicationType;

import org.apache.commons.collections.Predicate;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OldPublicationVO extends VersionedObjectsBase implements
		IPersistentOldPublication {

	public List readAllByTeacherIdAndOldPublicationType(final Integer teacherId,
			final OldPublicationType oldPublicationType) throws ExcepcaoPersistencia {
		
		Teacher teacher = (Teacher) readByOID(Teacher.class, teacherId);
		List oldPublications = (List) CollectionUtils.select(teacher.getAssociatedOldPublications(), new Predicate() {

			public boolean evaluate(Object obj) {
				if(obj instanceof OldPublication) {
					OldPublication oldPub = (OldPublication) obj;
					return oldPub.getOldPublicationType().equals(oldPublicationType);
				}
				return false;
			}
			
		});
		return oldPublications;
	}

}
