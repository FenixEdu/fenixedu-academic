/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Lesson;

/**
 * @author João Mota
 *  
 */
public class InfoLessonWithInfoExecutionCourse extends InfoLesson {

    public void copyFromDomain(Lesson lesson) {
        super.copyFromDomain(lesson);
    }

    public static InfoLesson newInfoFromDomain(Lesson lesson) {
        InfoLessonWithInfoExecutionCourse infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}