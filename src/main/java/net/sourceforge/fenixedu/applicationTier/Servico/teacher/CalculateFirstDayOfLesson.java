/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.Lesson;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class CalculateFirstDayOfLesson extends FenixService {

    @Service
    public static Calendar run(final Integer lessonId) {
        final Lesson lesson = rootDomainObject.readLessonByOID(lessonId);
        return lesson.getPeriod().getStartDate();
    }
}