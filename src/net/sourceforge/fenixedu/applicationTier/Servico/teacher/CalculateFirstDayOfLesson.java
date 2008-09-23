/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Lesson;

/**
 * @author Luis Cruz
 * 
 */
public class CalculateFirstDayOfLesson extends FenixService {

    public Calendar run(final Integer lessonId) {
	final Lesson lesson = rootDomainObject.readLessonByOID(lessonId);
	return lesson.getPeriod().getStartDate();
    }
}