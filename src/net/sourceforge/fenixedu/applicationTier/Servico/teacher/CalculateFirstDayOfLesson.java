/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 *  
 */
public class CalculateFirstDayOfLesson extends Service {

    public Calendar run(final Integer lessonId) throws ExcepcaoPersistencia {
	final Lesson lesson = rootDomainObject.readLessonByOID(lessonId);
	return lesson.getPeriod().getStartDate();
    }
}