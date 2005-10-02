/*
 * Created on 2/Out/2005 - 18:50:38
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.IOrientation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentOrientation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;
import net.sourceforge.fenixedu.util.OrientationType;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class OrientationVO extends VersionedObjectsBase implements
		IPersistentOrientation {

	public IOrientation readByTeacherIdAndOrientationType(Integer teacherId,
			OrientationType orientationType) throws ExcepcaoPersistencia {
		
		ITeacher teacher = (ITeacher) readByOID(Teacher.class, teacherId);
		for(IOrientation orientation : teacher.getAssociatedOrientations()) {
			if(orientation.getOrientationType().equals(orientationType))
				return orientation;
		}
		return null;
	}

}
