/*
 * Created on 11/Set/2005 - 18:42:01
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.ServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentServiceProviderRegime;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class ServiceProviderRegimeVO extends VersionedObjectsBase implements
		IPersistentServiceProviderRegime {

	public ServiceProviderRegime readByTeacherId(Integer teacherId)
			throws ExcepcaoPersistencia {

		Teacher teacher = (Teacher) readByOID(Teacher.class, teacherId);
		ServiceProviderRegime res = teacher.getServiceProviderRegime();
		
		return res;
	}

}
