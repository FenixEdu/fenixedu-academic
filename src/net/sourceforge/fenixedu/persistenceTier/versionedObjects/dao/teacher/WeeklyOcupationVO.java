/*
 * Created on 11/Set/2005 - 18:29:10
 * 
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.teacher;

import java.util.List;

import net.sourceforge.fenixedu.domain.teacher.IWeeklyOcupation;
import net.sourceforge.fenixedu.domain.teacher.WeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.teacher.IPersistentWeeklyOcupation;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class WeeklyOcupationVO extends VersionedObjectsBase implements
		IPersistentWeeklyOcupation {

	public IWeeklyOcupation readByTeacherId(Integer teacherId)
			throws ExcepcaoPersistencia {

		List<IWeeklyOcupation> weeklyOcupationList = (List<IWeeklyOcupation>) readAll(WeeklyOcupation.class);
		for(IWeeklyOcupation wo : weeklyOcupationList) {
			if(wo.getTeacher().getIdInternal().equals(teacherId))
				return wo;
		}
		return null;
	}

}
