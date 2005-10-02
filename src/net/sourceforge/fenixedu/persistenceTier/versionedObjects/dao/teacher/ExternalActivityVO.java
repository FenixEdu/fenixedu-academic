/*
 * Created on 11/Set/2005 - 18:03:38
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.IExternalActivity;
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

		ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherId);	
		List<IExternalActivity> res = teacher.getAssociatedExternalActivities();
		return res;

	}

}
