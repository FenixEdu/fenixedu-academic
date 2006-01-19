/*
 * Created on 2003/07/30
 *
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.sop;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoLesson;
import net.sourceforge.fenixedu.dataTransferObject.InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Luis Cruz & Sara Ribeiro
 * 
 * 
 */
public class ReadLessonByOID extends Service {

	public InfoLesson run(Integer oid) throws FenixServiceException, ExcepcaoPersistencia {

		InfoLesson result = null;

		ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
		IAulaPersistente lessonDAO = sp.getIAulaPersistente();
		Lesson lesson = (Lesson) lessonDAO.readByOID(Lesson.class, oid);
		if (lesson != null) {
			InfoLesson infoLesson = InfoLessonWithInfoRoomAndInfoRoomOccupationAndInfoPeriod
					.newInfoFromDomain(lesson);

			result = infoLesson;
		}

		return result;
	}
}