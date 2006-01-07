/*
 * Created on 2/Out/2005 - 18:50:38
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.teacher.Orientation;
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

	public Orientation readByTeacherIdAndOrientationType(Integer teacherId,
			OrientationType orientationType) throws ExcepcaoPersistencia {
		
		Teacher teacher = (Teacher) readByOID(Teacher.class, teacherId);
		for(Orientation orientation : teacher.getAssociatedOrientations()) {
			if(orientation.getOrientationType().equals(orientationType))
				return orientation;
		}
		return null;
	}

}
