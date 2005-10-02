/*
 * Created on 11/Set/2005 - 18:42:01
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.IServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ServiceProviderRegimeVO extends VersionedObjectsBase implements
		IPersistentServiceProviderRegime {

	public IServiceProviderRegime readByTeacherId(Integer teacherId)
			throws ExcepcaoPersistencia {

		ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherId);
		IServiceProviderRegime res = teacher.getServiceProviderRegime();
		
		return res;
	}

}
