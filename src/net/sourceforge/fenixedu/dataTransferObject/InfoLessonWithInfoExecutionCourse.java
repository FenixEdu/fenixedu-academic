/*
 * Created on Jun 7, 2004
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ILesson;

/**
 * @author João Mota
 *  
 */
public class InfoLessonWithInfoExecutionCourse extends InfoLesson {

    public void copyFromDomain(ILesson lesson) {
        super.copyFromDomain(lesson);
    }

    public static InfoLesson newInfoFromDomain(ILesson lesson) {
        InfoLessonWithInfoExecutionCourse infoLesson = null;
        if (lesson != null) {
            infoLesson = new InfoLessonWithInfoExecutionCourse();
            infoLesson.copyFromDomain(lesson);
        }
        return infoLesson;
    }

}