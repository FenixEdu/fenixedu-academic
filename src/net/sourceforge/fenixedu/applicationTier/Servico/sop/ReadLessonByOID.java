/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID extends Service {

	public InfoLesson run(Integer oid) throws FenixServiceException, ExcepcaoPersistencia {
		InfoLesson result = null;

		Lesson lesson = rootDomainObject.readLessonByOID(oid);
		if (lesson != null) {
			InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod
					.newInfoFromDomain(lesson);

			result = infoLesson;
		}

		return result;
	}
}