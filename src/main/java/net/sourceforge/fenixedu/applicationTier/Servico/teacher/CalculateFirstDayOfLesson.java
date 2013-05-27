/*
 * Created on 2004/02/17
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.Calendar;

import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Luis Cruz
 * 
 */
public class CalculateFirstDayOfLesson {

    @Service
    public static Calendar run(final Integer lessonId) {
        final Lesson lesson = RootDomainObject.getInstance().readLessonByOID(lessonId);
        return lesson.getPeriod().getStartDate();
    }
}