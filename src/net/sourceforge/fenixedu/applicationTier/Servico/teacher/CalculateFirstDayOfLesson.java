/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.ILesson;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IAulaPersistente;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class CalculateFirstDayOfLesson implements IService {

	public Calendar run(Integer lessonId) throws ExcepcaoPersistencia {
	    ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
	    IAulaPersistente persistentLesson = persistentSupport.getIAulaPersistente();

	    ILesson lesson = (ILesson) persistentLesson.readByOID(Lesson.class, lessonId);
	    Calendar startDate = lesson.getRoomOccupation().getPeriod().getStartDate();
	    return startDate;
    }

}