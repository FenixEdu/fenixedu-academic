/*
 * Created on 11/Set/2005 - 18:03:38
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentExternalActivity;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ExternalActivityVO extends VersionedObjectsBase implements
		IPersistentExternalActivity {

	public List readByTeacherId(Integer teacherId) throws ExcepcaoPersistencia {

		Teacher teacher = (Teacher) readByOID(Teacher.class, teacherId);	
		List<ExternalActivity> res = teacher.getAssociatedExternalActivities();
		return res;

	}

}
